package com.lowes.lowesForGeeks.controller;

import com.lowes.lowesForGeeks.Constants;
import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import com.lowes.lowesForGeeks.service.MemberService;
import com.lowes.lowesForGeeks.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/lowesforgeeks/team")
    Iterable<Team> read(
            @RequestHeader(name = "loggedInMemberId") Integer id) {
        if (memberService.findById(id).isPresent()) {
            return teamService.findAll();
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @GetMapping("lowesforgeeks/team/{id}")
    Optional<Team> findByTeamId(
            @RequestHeader(name = "loggedInMemberId") Integer loggingId,
            @PathVariable Integer id) {
        if (memberService.findById(loggingId).isPresent()) {
            if (teamService.findByTeamId(id).isPresent()) {
                return teamService.findByTeamId(id);
            }
            else {
                throw new NoSuchElementException("No such team exits");
            }
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @GetMapping("lowesforgeeks/team/search")
    Iterable<Team> findByTeamName(
            @RequestHeader(name = "loggedInMemberId") Integer loggingId,
            @RequestParam(name = "name", required = true) String name) {
        if (memberService.findById(loggingId).isPresent()) {
            Iterable<Team> team = teamService.findByTeamName(name);
            if (Iterables.size(team) > 0) {
                return team;
            }
            else {
                throw new NoSuchElementException("no such team exits");
            }
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    /*
       At the time of creation of team, if you want to add a existing member from DataBase, then provide his/her Id in to the
       toBeUpdatedMemberId header and keep Request Body Member as null. And if you want to add a new member then keep
       toBeUpdatedMemberId header as null and provide Request Body Member details.
       while providing details of REQUEST BODY member keep MEMBER TEAM ID and Team's Team Id SAME.
  AND IF BOTH ABOVE FIELD IS KEPT EMPTY ,ORGANIZATION ADMIN WHICH WILL BE CREATING TEAM WILL BE ADDED BY DEFAULT in the new team
      */
    @PostMapping("lowesforgeeks/team")
    ResponseEntity<Team> create(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @Nullable @RequestHeader(name = "toBeUpdatedMemberId") Integer memberToBeAddedAsTeamAdmin,
            @Valid @RequestBody Team team) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (memberToBeAddedAsTeamAdmin != null) {      //adding existing member from database
                if (memberService.findById(memberToBeAddedAsTeamAdmin).isPresent()) {
                    return teamService.create(team, memberService.findById(memberToBeAddedAsTeamAdmin).get(),loggedInMember);
                }
                else {
                    throw new NoSuchElementException("Enter correct id of member which has to be added in the new team");
                }
            }

            /*
            else if(member !=null){       //creating a new member(also will be team admin) at the creation of new team.
                return teamService.create(team,member,creator);
            }
             */

            else {     //if don't want to add any member(existing or new) by default organization admin will be added.
                return teamService.create(team,loggedInMember,loggedInMember);
            }
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }


    @PutMapping("/lowesforgeeks/team/update")
    ResponseEntity<Team> update(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestBody Team team) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (teamService.findByTeamId(team.getTeamId()).isPresent()) {
                return new ResponseEntity(teamService.update(team,loggedInMember), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PutMapping("/lowesforgeeks/team/updateTeamName/{teamName}")
    ResponseEntity<Team> updateName(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "toBeUpdatedTeamId") Integer toBeUpdatedId,
            @PathVariable String teamName) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (teamService.findByTeamId(toBeUpdatedId).isPresent()) {
                Team toBeUpdatedTeam = teamService.findByTeamId(toBeUpdatedId).get();
                return teamService.updateName(toBeUpdatedTeam, teamName, loggedInMember);
            }
            else {
                throw new NoSuchElementException("Such member doesn't exist in the DataBase");
            }
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    //to add a member in a team
    @PutMapping("lowesforgeeks/team/update/add/{memberId}")
    ResponseEntity<Team> addMember(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @RequestHeader(name = "teamId") Integer newMemberTeamId,
            @PathVariable Integer memberId) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (memberService.findById(memberId).isPresent()) {
                Member newMemberInTeam = memberService.findById(memberId).get();
                if (teamService.findByTeamId(newMemberTeamId).isPresent()) {
                    Team team = teamService.findByTeamId(newMemberTeamId).get();
                    return teamService.addMember(team, newMemberInTeam, loggedInMember);
                }
                else {
                    throw new NoSuchElementException("Enter correct Team Id as such team doesn't exist in DataBase");
                }
            }
            else {
                throw new NoSuchElementException("Such member doesn't exist in DataBase");
            }
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    //to remove any member from a team if he/she is present.
    @PutMapping("lowesforgeeks/team/update/remove/{memberId}")
    ResponseEntity<Team> removeMember(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer memberId) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (memberService.findById(memberId).isPresent()) {
                Member toBeRemoved = memberService.findById(memberId).get();
                if (teamService.findByTeamId(toBeRemoved.getTeamId()).isPresent()) {
                    Team team = teamService.findByTeamId(toBeRemoved.getTeamId()).get();
                    return teamService.removeMember(team, toBeRemoved, loggedInMember);
                }
                else {
                    throw new NoSuchElementException("Member hasn't be assigned to any team");
                }
            }
            else {
                throw new NoSuchElementException("Enter correct id of member to be removed");
            }
        }
        else {
            throw new NoSuchElementException(Constants.loggingMember);
        }
    }

    @PutMapping("lowesforgeeks/team/update/makeTeamAdmin/{memberId}")
    ResponseEntity makeTeamAdmin(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer memberId) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember =memberService.findById(id).get();
            if(memberService.findById(memberId).isPresent()) {
                Member toBeAdmin =memberService.findById(memberId).get();
                if(teamService.findByTeamId(toBeAdmin.getTeamId()).isPresent()) {
                    Team team = teamService.findByTeamId(toBeAdmin.getTeamId()).get();
                    return new ResponseEntity(teamService.addTeamAdmin(team, toBeAdmin, loggedInMember), HttpStatus.OK);
                }
                else{
                    throw new ValidationException("Member is not added in any team");
                }
            }
            else{
                throw new ValidationException("Such Member don't exist in database");
            }
        }
        else {
            throw new ValidationException(Constants.loggingMember);
        }
    }

    @PutMapping("lowesforgeeks/team/update/removeTeamAdmin/{memberId}")
    ResponseEntity removeTeamAdmin(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable Integer memberId) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember =memberService.findById(id).get();
            if(memberService.findById(memberId).isPresent()) {
                Member toBeNotAdmin =memberService.findById(memberId).get();
                if(teamService.findByTeamId(toBeNotAdmin.getTeamId()).isPresent()) {
                    Team team = teamService.findByTeamId(toBeNotAdmin.getTeamId()).get();
                    return new ResponseEntity(teamService.removeTeamAdmin(team, toBeNotAdmin, loggedInMember), HttpStatus.OK);
                }
                else{
                    throw new ValidationException("Member is not added in any team");
                }
            }
            else{
                throw new ValidationException("Such Member don't exist in database");
            }
        }
        else {
            throw new ValidationException(Constants.loggingMember);
        }
    }

    @DeleteMapping("lowesforgeeks/team/{teamId}")
    ResponseEntity delete(
            @RequestHeader(name = "loggedInMemberId") Integer id,
            @PathVariable(name = "teamId") Integer teamId) {
        if (memberService.findById(id).isPresent()) {
            Member loggedInMember = memberService.findById(id).get();
            if (loggedInMember.isOrganizationAdmin() || loggedInMember.isTeamAdmin() && loggedInMember.getTeamId() == teamId) {
                if (teamService.findByTeamId(teamId).isPresent()) {
                    teamService.delete(teamService.findByTeamId(teamId).get());     //teamService.delete(team);
                    return new ResponseEntity("Delete Successful", HttpStatus.OK);
                }
                else {
                    throw new NoSuchElementException("No such team exits");
                }
            }
            else {
                throw new ValidationException("Only team admins of same team or organization admins can delete a team");
            }
        }
        else {
            throw new ValidationException(Constants.loggingMember);
        }
    }
}
