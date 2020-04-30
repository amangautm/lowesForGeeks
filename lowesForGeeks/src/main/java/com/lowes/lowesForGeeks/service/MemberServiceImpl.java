package com.lowes.lowesForGeeks.service;

import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import com.lowes.lowesForGeeks.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamService teamService;

    @Override
    public Iterable<Member> findByFirstNameAndLastName(String firstName, String lastName) {
        return memberRepository.findByFirstNameAndLastName(firstName,lastName);
    }

    @Override
    public Iterable<Member> findByFirstName(String firstName) {
        return memberRepository.findByFirstName(firstName);
    }

    @Override
    public Iterable<Member> findByLastName(String lastName) {
        return memberRepository.findByLastName(lastName);
    }

    @Override
    public Iterable<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> findById(Integer number) {
        return memberRepository.findById(number);
    }


    @Override
    public ResponseEntity changeFirstName(Member user, Member toBeUpdated, String firstName) {
        if(user.isOrganizationAdmin() || (user.isTeamAdmin() && user.getTeamId() == toBeUpdated.getTeamId())
                || user.getId()==toBeUpdated.getId() )
        {
            toBeUpdated.setFirstName(firstName);
            return new ResponseEntity(memberRepository.save(toBeUpdated), OK);
        }
        else
            throw new ValidationException("Not Authorized");
    }

    @Override
    public ResponseEntity changeLastName(Member user, Member toBeUpdated, String lastName) {
        if(user.isOrganizationAdmin() || (user.isTeamAdmin() && user.getTeamId() == toBeUpdated.getTeamId())
                || user.getId()==toBeUpdated.getId() )
        {
            toBeUpdated.setLastName(lastName);
            return new ResponseEntity(memberRepository.save(toBeUpdated), OK);
        }
        else
            throw new ValidationException("Not Authorized");
    }

    @Override
    public ResponseEntity changeEmail(Member user, Member toBeUpdated, String email) {
        if(user.isOrganizationAdmin() || (user.isTeamAdmin() && user.getTeamId() == toBeUpdated.getTeamId())
                || user.getId()==toBeUpdated.getId() )
        {
            toBeUpdated.setEmail(email);
            return new ResponseEntity(memberRepository.save(toBeUpdated), OK);
        }
        else
            throw new ValidationException("Not Authorized");
    }
    
    private boolean hasAccess(Member member, Team team) {
        if (member.isOrganizationAdmin() || (member.isTeamAdmin()) && (member.getTeamId() == team.getTeamId())){
            return true;
    }
        else {
            return false;
        }
    }
    
    @Override
    public ResponseEntity makeTeamAdmin(Member toBeAdmin, Member member) {
        if(toBeAdmin.getTeamId()==null) {
            throw new ValidationException("Member has to be a team member first");
        }
        if(!hasAccess(member,teamService.findByTeamId(toBeAdmin.getTeamId()).get())) {
            return new ResponseEntity("NOT AUTHORIZED", BAD_REQUEST);
        }
        toBeAdmin.setTeamAdmin(true);
        return new ResponseEntity(memberRepository.save(toBeAdmin), OK);
    }

    @Override
    public ResponseEntity removeTeamAdmin(Member toBeNotAdmin, Member member) {
        if(hasAccess(member,teamService.findByTeamId(toBeNotAdmin.getTeamId()).get())) {
              toBeNotAdmin.setTeamAdmin(false);
             return new ResponseEntity(memberRepository.save(toBeNotAdmin), OK);
        }
        throw new ValidationException("NOT AUTHORIZED");
    }

    @Override
    public ResponseEntity makeOrganizationAdmin(Member toBeAdmin, Member member) {
        if(member.isOrganizationAdmin()){
            toBeAdmin.setOrganizationAdmin(true);
            return new ResponseEntity(memberRepository.save(toBeAdmin), OK);
        }
        else {
            throw new ValidationException("NOT AUTHORIZED");
        }
    }

    @Override
    public ResponseEntity removeOrganizationAdmin(Member toBeNotAdmin, Member member) {
        if(member.isOrganizationAdmin()){
            toBeNotAdmin.setOrganizationAdmin(false);
            return new ResponseEntity(memberRepository.save(toBeNotAdmin), OK);
        }
        else {
            throw new ValidationException("NOT AUTHORIZED");
        }
    }

    @Override
    public ResponseEntity create(Member member, Member adder, Team team) {
        if(team!=null) {
            if (!hasAccess(adder, team))
                throw new ValidationException("Member can be added only by team admins and organizations admins.");
            Integer teamId = team.getTeamId();
            member.setTeamId(teamId);
        }
        Integer organizationId = adder.getOrganizationId();
        member.setOrganizationId(organizationId);
        return new ResponseEntity(memberRepository.save(member), OK);

    }

    @Override
    public ResponseEntity delete(Member member) {
        memberRepository.delete(member);
        return new ResponseEntity("Delete Successful", OK);
    }
    
}
