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

    @Override
    public ResponseEntity create(Team team, Member member) {

        if(!member.isOrganizationAdmin()) {
            throw new ValidationException("Member is not an Organization admin,can't create a new Team.");
        }
        List<Member> memberList = team.getMemberList();
        memberList.add(member);
        team.setMemberList(memberList);
        member.setTeamAdmin(true);
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);

    }

    @Override
    public ResponseEntity updateName(Team team, String name, Member member) {

        if(!isQualified(member,team)) {
            throw new ValidationException("Not a team or organization admin");
        }
        team.setTeamName(name);
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @Override
    public ResponseEntity addMemberExist(Team team, Member memberNew, Member member) {

        if(!isQualified(member, team)) {
            throw new ValidationException("Not a team or organization admin");
        }
        if(memberNew.getTeamId() != null) {
            throw  new ValidationException("Member already part of a team");
        }
        List<Member> memberList=team.getMemberList();
        memberList.add(memberNew);
        team.setMemberList(memberList);
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @Override
    public ResponseEntity addMemberNew(Team team, Member toBeAdded, Member updater) {
        if (!isQualified(updater, team)){
            throw new ValidationException("Not a team or organization admin");
    }
        ResponseEntity temp=  memberService.create(toBeAdded, updater , team);   //temp
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @Override
    public ResponseEntity removeMember(Team team, Member toBeRemoved, Member updater) {
        if(!isQualified(updater, team)) {
            throw new ValidationException("Not a team or organization admin");
        }
        ResponseEntity temp = memberService.delete(toBeRemoved);
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @Override
    public ResponseEntity addTeamAdmin(Team team, Member admin, Member updater) {
        if(!isQualified(updater, team)) {
            throw new ValidationException("Not a team or organization admin");
        }
        memberService.makeTeamAdmin(admin, updater);
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @Override
    public ResponseEntity removeTeamAdmin(Team team, Member admin, Member updater) {
        if(!isQualified(updater, team)) {
            throw new ValidationException("Not a team or organization admin");
        }
        memberService.removeTeamAdmin(admin, updater);
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @Override
    public Iterable<Team> findAll() {
        return teamRepository.findAll();
    }

    @Override
    public Optional<Team> findByTeamId(Integer id) {
        return teamRepository.findById(id);
    }

    @Override
    public Iterable<Team> findByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }

    @Override
    public ResponseEntity delete(Team team) {
        teamRepository.delete(team);
        return new ResponseEntity("Deleted", HttpStatus.OK);
    }

    private boolean isQualified(Member updater, Team team)
    {
        if((!updater.isTeamAdmin() && !updater.isOrganizationAdmin()) ||
                (updater.isTeamAdmin() && !updater.isOrganizationAdmin() && updater.getTeamId()!=team.getTeamId())) {
            return false;
        }
        else {
            return true;
        }
    }
}
