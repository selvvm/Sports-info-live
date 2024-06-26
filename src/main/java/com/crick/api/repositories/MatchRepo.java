package com.crick.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.crick.api.entities.Match;

public interface MatchRepo extends JpaRepository<Match, Integer>{

    Optional<Match> findByTeamHeading(String teamHeading);
}
