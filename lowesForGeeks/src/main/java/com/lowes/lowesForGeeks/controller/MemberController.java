package com.lowes.lowesForGeeks.controller;

import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import com.lowes.lowesForGeeks.service.MemberService;
import com.lowes.lowesForGeeks.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private TeamService teamService;

    @Autowired
    public void setMemberService(MemberService memberService){ this.memberService = memberService; }

    @Autowired
    public void setTeamService(TeamService teamService){ this.teamService = teamService; }

    @GetMapping("/lowesforgeeks/member")
    Iterable<Member> read(){
        return memberService.findAll();
    }

    @GetMapping("/lowesforgeeks/member/{id}")
    Optional<Member> findById(@PathVariable Integer id){
        if(memberService.findById(id).isPresent()) {
            return memberService.findById(id);
        }
        else{
            throw new NoSuchElementException("Such member doesn't exist in the DataBase");
        }
    }

    @GetMapping("/lowesforgeeks/member/search")
    Iterable<Member> findByName(
            @RequestParam(value = "first", required = false) String firstName,
            @RequestParam(value = "last", required = false) String lastName) {
        if(firstName!=null&&lastName!=null) {
            return memberService.findByFirstNameAndLastName(firstName, lastName);
        }
        else if(firstName!=null) {
            return memberService.findByFirstName(firstName);
        }
        else if(lastName!=null) {
            return memberService.findByLastName(lastName);
        }
        else {
            throw new NoSuchElementException("Such member doesn't exist in the DataBase");
        }
    }

    @PostMapping("/lowesforgeeks/member")
    ResponseEntity<Member> create(
            @RequestHeader( name="loggedInMemberId") Integer id,
            @RequestHeader(name = "teamId") Integer teamId,
            @Valid  @RequestBody Member newMember){
        if (memberService.findById(id).isPresent()) {
            Member existingMember = memberService.findById(id).get();
            if (existingMember.getTeamId() != teamId) {
                throw new NoSuchElementException("Enter correct team id of logged in member");
            }
            else {
                return memberService.create(newMember, existingMember, teamService.findByTeamId(teamId).get());
            }
        }
        else{
            throw new NoSuchElementException("Enter correct id of logged in member");
        }
    }

    @PutMapping("/lowesforgeeks/member/update")
    ResponseEntity<Member> update(
            @RequestHeader( name="loggedInMemberId") Integer id,
            @RequestHeader(name = "toBeUpdatedMemberId") Integer toBeUpdatedId,
            @Valid  @RequestBody Member member){
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember =memberService.findById(id).get();
            if(memberService.findById(toBeUpdatedId).isPresent()) {
                Member toBeUpdatedMember =memberService.findById(toBeUpdatedId).get();
                return memberService.update(loggedInMember,toBeUpdatedMember,member);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else {
            throw new NoSuchElementException("Enter correct id of logged in member");
        }
    }

    @PutMapping("/lowesforgeeks/member/updateFirstName/{firstName}")
    ResponseEntity<Member> changeFirstName(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "toBeUpdatedMemberId") Integer toBeUpdatedId,
            @PathVariable String firstName) {
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember =memberService.findById(id).get();
            if(memberService.findById(toBeUpdatedId).isPresent()) {
                Member toBeUpdatedMember =memberService.findById(toBeUpdatedId).get();
                return memberService.changeFirstName(loggedInMember, toBeUpdatedMember , firstName);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else {
            throw new NoSuchElementException("Enter correct id of logged in member");
        }
    }

    @PutMapping("/lowesforgeeks/member/updateLastName/{lastName}")
    ResponseEntity<Member> changeLastName(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "toBeUpdatedMemberId") Integer toBeUpdatedId,
            @PathVariable String lastName) {
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember =memberService.findById(id).get();
            if(memberService.findById(toBeUpdatedId).isPresent()) {
                Member toBeUpdatedMember =memberService.findById(toBeUpdatedId).get();
                return memberService.changeLastName(loggedInMember, toBeUpdatedMember , lastName);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else {
            throw new NoSuchElementException("Enter correct id of logged in member");
        }
    }

    @PutMapping("/lowesforgeeks/member/updateEmail/{email}")
    ResponseEntity<Member> changeEmail(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "toBeUpdatedMemberId") Integer toBeUpdatedId,
            @PathVariable String email) {
        if(memberService.findById(id).isPresent()) {
            Member loggedInMember =memberService.findById(id).get();
            if(memberService.findById(toBeUpdatedId).isPresent()) {
                Member toBeUpdatedMember =memberService.findById(toBeUpdatedId).get();
                return memberService.changeEmail(loggedInMember, toBeUpdatedMember , email);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else {
            throw new NoSuchElementException("Enter correct id of logged in member");
        }
    }

    @DeleteMapping("/lowesforgeeks/member/{toBeDeletedId}")
    ResponseEntity delete(
            @RequestHeader(name="loggedInMemberId") Integer id,
            @RequestHeader(name = "teamId") Integer teamId,
            @PathVariable Integer toBeDeletedId) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (loggedInMember.getTeamId() != teamId) {
                throw new NoSuchElementException("Enter correct team id of logged in member");
            }
            if (memberService.findById(toBeDeletedId).isPresent()) {
                Member toBeDeletedMember = memberService.findById(toBeDeletedId).get();
                if (loggedInMember.isOrganizationAdmin()) {
                    return memberService.delete(toBeDeletedMember);
                } else if (loggedInMember.isTeamAdmin()) {
                    if (loggedInMember.getTeamId() == toBeDeletedMember.getTeamId()) {
                        return memberService.delete(toBeDeletedMember);
                    } else {
                        throw new NoSuchElementException("As a Team Admin not have access to delete member from other team");
                    }
                } else {
                    throw new NoSuchElementException("As a Normal member not have access to delete any member");
                }
            }
         else {
            throw new NoSuchElementException("Such member doesn't exist in the DataBase");
        }
    }
         else{
             throw new NoSuchElementException("Enter correct member id");
         }
    }

    @PutMapping("/lowesforgeeks/member/makeOrganizationAdmin/{toBeAdminId}")
    ResponseEntity<Member> makeOrganizationAdmin(
            @RequestHeader(name ="loggedInMemberId") Integer id,
            @PathVariable Integer toBeAdminId){
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (memberService.findById(toBeAdminId).isPresent()) {
                Member toBeAdminMember = memberService.findById(toBeAdminId).get();
                return new ResponseEntity(memberService.makeOrganizationAdmin(toBeAdminMember,loggedInMember),HttpStatus.OK);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else{
            throw new NoSuchElementException("Enter correct member id");
        }
    }

    @PutMapping("/lowesforgeeks/member/removeOrganizationAdmin/{toNotBeAdminId}")
    ResponseEntity<Member> removeOrganizationAdmin(
            @RequestHeader(name ="loggedInMemberId") Integer id,
            @PathVariable Integer toNotBeAdminId){
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (memberService.findById(toNotBeAdminId).isPresent()) {
                Member toNotBeAdminMember = memberService.findById(toNotBeAdminId).get();
                return new ResponseEntity(memberService.removeOrganizationAdmin(toNotBeAdminMember,loggedInMember),HttpStatus.OK);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else{
            throw new NoSuchElementException("Enter correct member id");
        }
    }

    @PutMapping("/lowesforgeeks/member/makeTeamAdmin/{toBeAdminId}")
    ResponseEntity<Member> makeTeamAdmin(
            @RequestHeader(name ="loggedInMemberId") Integer id,
            @PathVariable Integer toBeAdminId){
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (memberService.findById(toBeAdminId).isPresent()) {
                Member toBeAdminMember = memberService.findById(toBeAdminId).get();
                return new ResponseEntity(memberService.makeTeamAdmin(toBeAdminMember,loggedInMember),HttpStatus.OK);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else{
            throw new NoSuchElementException("Enter correct member id");
        }
    }

    @PutMapping("/lowesforgeeks/member/removeTeamAdmin/{toNotBeAdminId}")
    ResponseEntity<Member> removeTeamAdmin(
            @RequestHeader(name ="loggedInMemberId") Integer id,
            @PathVariable Integer toNotBeAdminId){
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (memberService.findById(toNotBeAdminId).isPresent()) {
                Member toNotBeAdminMember = memberService.findById(toNotBeAdminId).get();
                return new ResponseEntity(memberService.removeTeamAdmin(toNotBeAdminMember,loggedInMember),HttpStatus.OK);
            }
            else{
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else{
            throw new NoSuchElementException("Enter correct member id");
        }
    }
}
