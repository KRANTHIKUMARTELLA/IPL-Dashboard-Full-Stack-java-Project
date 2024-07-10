package com.ipl.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String teamName;
    private Long totalMatches;
    private Long totalWins;
    
    @Transient
    private List<MatchTeam> matches;

     public List<MatchTeam> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchTeam> matches) {
		this.matches = matches;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(Long totalMatches) {
        this.totalMatches = totalMatches;
    }

    public Long getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(Long totalWins) {
        this.totalWins = totalWins;
    }

    public Team(String teamName, Long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
    
    

    public Team(String teamName, Long totalMatches, Long totalWins) {
		super();
		this.teamName = teamName;
		this.totalMatches = totalMatches;
		this.totalWins = totalWins;
	}

	public Team() {

    }

    @Override
    public String toString() {
        return "Team [teamName=" + teamName + ", totalMatches=" + totalMatches + ", totalWins=" + totalWins + "]";
    }

}
