package com.lowes.lowesForGeeks.service;

import com.lowes.lowesForGeeks.model.Member;
import com.lowes.lowesForGeeks.model.Team;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface TeamService {
    ResponseEntity<Team> create(Team team, Member creator);

    ResponseEntity<Team> updateName(Team team, String name,Member updater);

    ResponseEntity<Team> addMemberExist(Team team, Member toBeAdded, Member updater );

    ResponseEntity<Team> addMemberNew(Team team, Member toBeAdded, Member updater);

    ResponseEntity<Team> removeMember(Team team, Member toBeRemoved, Member updater);

    ResponseEntity<Team> addTeamAdmin(Team team, Member admin, Member updater);

    ResponseEntity<Team> removeTeamAdmin(Team team , Member admin, Member updater);

    Iterable<Team> findAll();

    Optional<Team> findByTeamId(Integer id);

    Iterable<Team> findByTeamName(String teamName);

    ResponseEntity delete(Team team);
}
