package com.lowes.lowesForGeeks.service;

import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import com.lowes.lowesForGeeks.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Service("teamService")
public class TeamServiceImpl implements TeamService{

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberService memberService;

    private String mssg ="Only Organization and Team Admin is authorized to do so.";


    @Override
    public Iterable<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public Optional<Team> findByTeamId(Integer id) {
        return teamRepository.findByTeamId(id);
    }

    @Override
    public Iterable<Team> findByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }

    @Override
    public ResponseEntity create(Team team, Member member, Member creator) {
        if(!creator.isOrganizationAdmin()) {
            throw new ValidationException("Member is not an Organization admin,can't create a new Team.");
        }
        if(!member.isOrganizationAdmin()) {
            member.setTeamId(team.getTeamId());
        }
        List<Member> memberList = team.getMemberList();
        memberList.add(member);
        team.setMemberList(memberList);
        member.setTeamAdmin(true);
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Team> update(Team team, Member creator) {
        if(MemberServiceImpl.hasAccess(creator,team)){
            return new ResponseEntity<>(teamRepository.save(team),HttpStatus.OK);
        }
        else{
            throw new ValidationException(mssg);
        }
    }

    @Override
    public ResponseEntity updateName(Team team, String name, Member creator) {
        if(MemberServiceImpl.hasAccess(creator,team)) {
            team.setTeamName(name);
            return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
        }
        else{
            throw new ValidationException(mssg);
        }
    }

    @Override
    public ResponseEntity addMember(Team team, Member toBeAdded, Member creator) {
        if(MemberServiceImpl.hasAccess(creator, team)) {
            toBeAdded.setTeamId(team.getTeamId());
            List<Member> memberList=team.getMemberList();
            memberList.add(toBeAdded);
            team.setMemberList(memberList);
            return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
        }
        else{
            throw new ValidationException(mssg);
        }
    }


    @Override
    public ResponseEntity removeMember(Team team, Member toBeRemoved, Member creator) {
        if(MemberServiceImpl.hasAccess(creator, team)) {
            toBeRemoved.setTeamId(null);
            return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
        }
        else{
            throw new ValidationException(mssg);
        }
    }

    @Override
    public ResponseEntity addTeamAdmin(Team team, Member admin, Member creator) {
        if(MemberServiceImpl.hasAccess(creator, team)) {
            memberService.makeTeamAdmin(admin, creator);
            return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
        }
        else{
            throw new ValidationException(mssg);
        }
    }

    @Override
    public ResponseEntity removeTeamAdmin(Team team, Member admin, Member creator) {
        if(MemberServiceImpl.hasAccess(creator, team)) {
            memberService.removeTeamAdmin(admin, creator);
            return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
        }
        else{
            throw new ValidationException(mssg);
        }
    }

    @Override
    public ResponseEntity delete(Team team) {
        teamRepository.delete(team);
        return new ResponseEntity("Deleted", HttpStatus.OK);
    }
}
