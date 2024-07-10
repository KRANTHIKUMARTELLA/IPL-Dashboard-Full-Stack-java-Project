package com.ipl.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipl.exception.ResourceNotFoundException;
import com.ipl.model.MatchTeam;
import com.ipl.model.Team;
import com.ipl.repository.MatchRepository;
import com.ipl.repository.TeamRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("/team")
public class TeamController {

	private TeamRepository teamRepository;
	private MatchRepository matchRepository;

	public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
		super();
		this.teamRepository = teamRepository;
		this.matchRepository = matchRepository;
	}
	
	@GetMapping("/teams")
	public List<Team> getAl(){
		return this.teamRepository.findAll();
	}
	

	@GetMapping("{teamName}")
	public ResponseEntity<Team> getTeam(@PathVariable("teamName") String teamName) {

		Team team = this.teamRepository.findByTeamNameIgnoreCase(teamName);
		if (team == null)
			throw new ResourceNotFoundException("team" + teamName);
		else
			team.setMatches(this.matchRepository.findLatestMatchesByTeam(teamName, 4));
		return ResponseEntity.ok(team);
	}
	
	

	@GetMapping
	public void sample() {
		System.out.println("this is sample");
	}

	@GetMapping("/team/{teamName}/matches")
	public List<MatchTeam> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {
		LocalDate startDate = LocalDate.of(year, 1, 1);
		LocalDate endDate = LocalDate.of(year + 1, 1, 1);
		return this.matchRepository.getMatchesByTeamBetweenDates(
				teamName, startDate, endDate);
	}

}
