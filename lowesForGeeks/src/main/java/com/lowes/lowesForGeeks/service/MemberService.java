package com.lowes.lowesForGeeks.service;

import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;

public interface MemberService {
    Iterable<Member> findByFirstNameAndLastName(String firstName, String lastName);

    Iterable<Member> findByFirstName(String firstName);

    Iterable<Member> findByLastName(String lastName);

    Iterable<Member>  findAll();

    Optional<Member> findById(Integer number);

    ResponseEntity<Member> create(Member member, Member creator, Integer newMemberTeamId, Integer newMemberOrgId);

    ResponseEntity<Member> delete(Member member);

    ResponseEntity<Member> update(Member loggedInMember, Member toBeUpdated, Member member);

    ResponseEntity<Member> changeFirstName(Member loggedInMember, Member toBeUpdated, String firstName);

    ResponseEntity<Member> changeLastName(Member loggedInMember, Member toBeUpdated, String lastName);

    ResponseEntity<Member> changeEmail(Member loggedInMember, Member toBeUpdated, String email);

    ResponseEntity<Member> makeTeamAdmin(Member toBeAdmin, Member member);

    ResponseEntity<Member> removeTeamAdmin(Member toBeNotAdmin, Member member);

    ResponseEntity<Member> makeOrganizationAdmin(Member toBeAdmin, Member member);

    ResponseEntity<Member> removeOrganizationAdmin(Member toBeNotAdmin, Member member);

}
