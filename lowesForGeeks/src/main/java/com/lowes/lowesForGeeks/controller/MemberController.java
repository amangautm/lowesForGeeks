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
    Iterable<Member> read()
    {
        return memberService.findAll();
    }

    @PostMapping("/lowesforgeeks/member")
    ResponseEntity<Member> create(
            @RequestHeader( name="loggedInMemberId") Integer id,
            @Nullable @RequestHeader(name = "teamId") Integer teamId, @Valid  @RequestBody Member member){
        Member adder = memberService.findById(id).get();
        return memberService.create(member,adder,teamService.findByTeamId(teamId).get());
    }

    @PutMapping("/lowesforgeeks/member/update/{firstName}")
    ResponseEntity<Member> changeFirstName(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "memberId") Integer memberId,@PathVariable String firstName) {
        Optional<Member> member = memberService.findById(memberId);
        if(member.isPresent()) {
            return memberService.changeFirstName(memberService.findById(id).get(),member.get(), firstName);
        }
        else {
            throw new NoSuchElementException("Member not found");
        }
    }

    @PutMapping("lowesforgeeks/member/update/{lastName}")
    ResponseEntity<Member> changeLastName(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "memberId") Integer memberId,@PathVariable String lastName) {
        Optional<Member> member = memberService.findById(memberId);
        if(member.isPresent())
            return memberService.changeLastName(memberService.findById(id).get(), member.get(),lastName);
        else
            throw new NoSuchElementException("Member not found");
    }

    @PutMapping("lowesforgeeks/member/update/{email}")
    ResponseEntity<Member> changeEmail(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "memberId") Integer memberId,@PathVariable String email) {
        Optional<Member> member = memberService.findById(memberId);
        if(member.isPresent())
            return memberService.changeEmail(memberService.findById(id).get(), member.get(),email);
        else
            throw new NoSuchElementException("Member not found");
    }

    @GetMapping("/lowesforgeeks/member/{id}")
    Optional<Member> findById(@PathVariable Integer id)
    {
        return memberService.findById(id);
    }

    @GetMapping("/lowesforgeeks/member/search")
    Iterable<Member> findByName(
            @RequestParam(value = "first", required = false) String firstName,
            @RequestParam(value = "last", required = false) String lastName) {
        if(firstName!=null&&lastName!=null)
            return memberService.findByFirstNameAndLastName(firstName, lastName);
        else if(firstName!=null)
            return memberService.findByFirstName(firstName);
        else if(lastName!=null)
            return memberService.findByLastName(lastName);
        else
            return null;
    }

    @DeleteMapping("lowesforgeeks/member")
    ResponseEntity delete(@RequestHeader(name = "loggedInMemberId") Integer id)
    {
        return memberService.delete(memberService.findById(id).get());
    }
}
