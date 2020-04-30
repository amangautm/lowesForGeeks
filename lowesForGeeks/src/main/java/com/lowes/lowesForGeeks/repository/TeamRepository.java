package com.lowes.lowesForGeeks.repository;

import com.lowes.lowesForGeeks.model.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team,Integer> {
      Iterable<Team> findAll();
      Optional<Team> findByTeamId(Integer id);
      Iterable<Team> findByTeamName(String teamName);
}
