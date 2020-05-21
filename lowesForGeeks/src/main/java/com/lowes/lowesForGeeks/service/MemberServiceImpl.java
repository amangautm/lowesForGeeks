package com.lowes.lowesForGeeks.service;

import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import com.lowes.lowesForGeeks.repository.MemberRepository;
import com.lowes.lowesForGeeks.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private OrganizationRepository organizationRepository;

    private String mssg ="Only Organization and Team Admin is authorized to do so.";

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
    public ResponseEntity<Member> update(Member loggedInMember, Member toBeUpdated, Member member) {
        if(loggedInMember.isOrganizationAdmin() || (loggedInMember.isTeamAdmin() && loggedInMember.getTeamId()
                == toBeUpdated.getTeamId()) || loggedInMember.getId()==toBeUpdated.getId() ){
            return new ResponseEntity(memberRepository.save(member), OK);
        }
        else{
            throw new ValidationException("Not Authorized");
        }
    }

    @Override
    public ResponseEntity changeFirstName(Member loggedInMember, Member toBeUpdated, String firstName) {
        if(loggedInMember.isOrganizationAdmin() || (loggedInMember.isTeamAdmin() && loggedInMember.getTeamId()
                == toBeUpdated.getTeamId())
                || loggedInMember.getId()==toBeUpdated.getId() ){
            toBeUpdated.setFirstName(firstName);
            return new ResponseEntity(memberRepository.save(toBeUpdated), OK);
        }
        else {
            throw new ValidationException("Not Authorized");
        }
    }

    @Override
    public ResponseEntity changeLastName(Member loggedInMember, Member toBeUpdated, String lastName) {
        if(loggedInMember.isOrganizationAdmin() || (loggedInMember.isTeamAdmin() && loggedInMember.getTeamId()
                == toBeUpdated.getTeamId()) || loggedInMember.getId()==toBeUpdated.getId() ){
            toBeUpdated.setLastName(lastName);
            return new ResponseEntity(memberRepository.save(toBeUpdated), OK);
        }
        else
            throw new ValidationException("Not Authorized");
    }

    @Override
    public ResponseEntity changeEmail(Member loggedInMember, Member toBeUpdated, String email) {
        if(loggedInMember.isOrganizationAdmin() || (loggedInMember.isTeamAdmin() && loggedInMember.getTeamId()
                == toBeUpdated.getTeamId()) || loggedInMember.getId()==toBeUpdated.getId() ){
            toBeUpdated.setEmail(email);
            return new ResponseEntity(memberRepository.save(toBeUpdated), OK);
        }
        else
            throw new ValidationException("Not Authorized");
    }
    
    static boolean hasAccess(Member member, Team team) {
        if (member.isOrganizationAdmin() || (member.isTeamAdmin()) && (member.getTeamId() == team.getTeamId())){
            return true;
    }
        else {
            return false;
        }
    }
    
    @Override
    public ResponseEntity makeTeamAdmin(Member toBeAdmin, Member member) {
        if(toBeAdmin.getTeamId() ==null) {
            throw new ValidationException("Member has to be a team member first");
        }
        if(hasAccess(member,teamService.findByTeamId(toBeAdmin.getTeamId()).get())) {
            toBeAdmin.setTeamAdmin(true);
            return new ResponseEntity(memberRepository.save(toBeAdmin), OK);
        }
        else{
            return new ResponseEntity("Only Organization Admin and Team Admin of same Team is Authorized", BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity removeTeamAdmin(Member toBeNotAdmin, Member member) {
        if(hasAccess(member,teamService.findByTeamId(toBeNotAdmin.getTeamId()).get())) {
              toBeNotAdmin.setTeamAdmin(false);
             return new ResponseEntity(memberRepository.save(toBeNotAdmin), OK);
        }
        else {
            return new ResponseEntity("Only Organization and Team Admin of same Team is Authorized", BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity makeOrganizationAdmin(Member toBeAdmin, Member creator) {
        if(creator.isOrganizationAdmin()){
            toBeAdmin.setOrganizationAdmin(true);
            return new ResponseEntity(memberRepository.save(toBeAdmin), OK);
        }
        else {
            return new ResponseEntity("Only Organization Admin is Authorized", BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity removeOrganizationAdmin(Member toBeNotAdmin, Member creator) {
        if(creator.isOrganizationAdmin()){
            toBeNotAdmin.setOrganizationAdmin(false);
            return new ResponseEntity(memberRepository.save(toBeNotAdmin), OK);
        }
        else {
            return new ResponseEntity("Only Organization Admin is Authorized", BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Member> create(Member newMember, Member creator, Integer newMemberTeamId, Integer newMemberOrgId) {
        if(creator.isOrganizationAdmin() || creator.isTeamAdmin()&& creator.getTeamId() ==newMemberTeamId) {
            if (newMemberOrgId == null) {
                if (teamService.findByTeamId(newMemberTeamId).isPresent()) {
                    newMember.setTeamId(newMemberTeamId);
                    newMember.setOrganizationId(creator.getOrganizationId());
                    return new ResponseEntity<>(memberRepository.save(newMember), OK);
                }
                else {
                    throw new NoSuchElementException("Such team doesn't exist in DataBase");
                }
            }
            else {
                if (organizationRepository.findById(newMemberOrgId).isPresent()) {
                    if (teamService.findByTeamId(newMemberTeamId).isPresent()) {
                        newMember.setTeamId(newMemberTeamId);
                        newMember.setOrganizationId(newMemberOrgId);
                        return new ResponseEntity<>(memberRepository.save(newMember), OK);
                    } else {
                        throw new NoSuchElementException("Such team doesn't exist in DataBase");
                    }
                }
                else {
                    throw new NoSuchElementException("Such organisation doesn't exist in DataBase");
                }
            }
        }
        else{
            throw new ValidationException("Not Authorized");
        }
    }


    @Override
    public ResponseEntity delete(Member member) {
        memberRepository.delete(member);
        return new ResponseEntity("Delete Successful", OK);
    }

}
