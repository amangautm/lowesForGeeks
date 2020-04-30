package com.lowes.lowesForGeeks.controller;

import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import com.lowes.lowesForGeeks.service.MemberService;
import com.lowes.lowesForGeeks.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Iterables;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private MemberService memberService;

    @Autowired
    public void setTeamService(TeamService teamService){ this.teamService = teamService; }

    @Autowired
    public void setMemberService(MemberService memberService){ this.memberService = memberService; }

    @GetMapping("/lowesforgeeks/team")
    Iterable<Team> read(){
        return teamService.findAll();
    }

    @GetMapping("lowesforgeeks/team/{id}")
    Optional<Team> read(@PathVariable Integer id){
        if(teamService.findByTeamId(id).isPresent()) {
            return teamService.findByTeamId(id);
        }
        else {
            throw new NoSuchElementException("no such team exits");
        }
    }

    @GetMapping("lowesforgeeks/team/search")
    Iterable<Team> read(@RequestParam(name = "name", required = true) String name){
        Iterable<Team> team =  teamService.findByTeamName(name);
        if(Iterables.size(team)>0){
            return team;
        }
        else {
            throw new NoSuchElementException("no such team exits");
        }
    }

    @PostMapping("lowesforgeeks/team")
    ResponseEntity<Team> create(
            @RequestHeader(name = "loggedInMemberId") Integer id, @Valid @RequestBody Team team){
        return teamService.create(team, memberService.findById(id).get());
    }


    @PutMapping("/lowesforgeeks/team")
    ResponseEntity<Team> update(
            @RequestHeader(name = "loggedInMemberId") Integer id,@RequestBody Team team){
           if(teamService.findByTeamId(team.getTeamId()).isPresent())
               return new ResponseEntity(teamService.create(team,memberService.findById(id).get()), HttpStatus.OK);
           else
               return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("lowesforgeeks/team/update/add/{memberId}")
    ResponseEntity<Team> addMember(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "teamId") Integer teamId, @PathVariable Integer memberId ){
        Member user= memberService.findById(id).get();
        Optional<Team> team= teamService.findByTeamId(teamId);
        if(!team.isPresent())
            throw new NoSuchElementException("TEAM NOT EXIST");
        Optional<Member> member= memberService.findById(memberId);
        if(!member.isPresent())
            throw new NoSuchElementException("Member not found");
        return teamService.addMemberExist(team.get(), member.get(), user);
    }

    @PutMapping("lowesforgeeks/team/update/remove/{memberId}")
    ResponseEntity<Team> removeMember(@RequestHeader(name = "loggedInMemberId") Integer id, @PathVariable Integer memberId)
    {
        Member user= memberService.findById(id).get();
        Optional<Team> team= teamService.findByTeamId(user.getTeamId());
        if(!team.isPresent())
            throw new NoSuchElementException("TEAM NOT EXIST");
        Optional<Member> member= memberService.findById(memberId);
        if(!member.isPresent())
            throw new NoSuchElementException("Member not found");
        return teamService.removeMember(team.get(), member.get(), user);
    }

    @PutMapping("lowesforgeeks/team/update/makeTeamAdmin/{memberId}")
    ResponseEntity makeTeamAdmin(@RequestHeader(name = "loggedInMemberId")Integer id, @PathVariable Integer memberId)
    {
        Member updater = memberService.findById(id).get();
        Optional<Member> toBeAdmin = memberService.findById(memberId);
        if(!toBeAdmin.isPresent())
            throw new NoSuchElementException("Member not found");
        Team team = null;
        if(toBeAdmin.get().getTeamId()!=null)
            team = teamService.findByTeamId(toBeAdmin.get().getTeamId()).get();
        return new ResponseEntity(teamService.addTeamAdmin(team,toBeAdmin.get(), updater ), HttpStatus.OK);
    }

    @PutMapping("lowesforgeeks/team/update/removeTeamAdmin/{memberId}")
    ResponseEntity removeTeamAdmin(@RequestHeader(name = "loggedInMemberId")Integer id, @PathVariable Integer memberId){
        Optional<Member> toBeNotAdmin = memberService.findById(memberId);
        if(!toBeNotAdmin.isPresent()) {
            throw new NoSuchElementException("Member not found");
        }
        Team team = null;
        if(toBeNotAdmin.get().getTeamId()!=null)
            team = teamService.findByTeamId(toBeNotAdmin.get().getTeamId()).get();
        return new ResponseEntity(
                teamService.removeTeamAdmin(team,toBeNotAdmin.get(), memberService.findById(id).get()), HttpStatus.OK);
    }

    @DeleteMapping("lowesforgeeks/team/{teamId}")
    ResponseEntity delete(@RequestHeader(name = "loggedInMemberId") Integer id, @PathVariable(name = "teamId") Integer teamId){
        Member user = memberService.findById(id).get();
        if(user.isOrganizationAdmin() || user.isTeamAdmin() && user.getTeamId()==teamId){
            if(teamService.findByTeamId(teamId).isPresent()){
                teamService.delete(teamService.findByTeamId(teamId).get());     //teamService.delete(team);
                return new ResponseEntity("Delete Successful", HttpStatus.OK);
            }
            else {
                throw new NoSuchElementException("no such team exits");
            }
        }
        throw new ValidationException("Only team admins of same team or organization admins can delete a team");
    }
}
