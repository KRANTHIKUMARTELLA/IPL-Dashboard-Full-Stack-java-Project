package com.ipl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ipl.model.Team;

public interface TeamRepository extends JpaRepository<Team,Long> {
	Team findByTeamNameIgnoreCase(String teamName);
	

}
