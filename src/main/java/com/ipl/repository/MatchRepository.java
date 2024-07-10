package com.ipl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ipl.model.MatchTeam;

public interface MatchRepository extends CrudRepository<MatchTeam, Long> {

  List<MatchTeam> getByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable pageable);

  @Query("select m from MatchTeam m where (m.team1 = :teamName or m.team2 = :teamName) and m.date between :date1 and :date2 order by m.date desc")
  List<MatchTeam> getMatchesByTeamBetweenDates(
      @Param("teamName") String teamName,
      @Param("date1") LocalDate date1,
      @Param("date2") LocalDate date2);

  // List<MatchTeam> getByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(
  // String teamName1, LocalDate date1, LocalDate date2,
  // String teamName2, LocalDate date3, LocalDate date4);

  default List<MatchTeam> findLatestMatchesByTeam(String team, int count) {
    return getByTeam1OrTeam2OrderByDateDesc(team, team, PageRequest.of(0, count));
  }

}
