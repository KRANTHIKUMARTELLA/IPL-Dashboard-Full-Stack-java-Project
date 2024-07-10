package com.ipl.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.ipl.model.MatchTeam;
import com.ipl.model.Team;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    
    @PersistenceContext
    private final EntityManager em;

    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    Map<String, Team> teamData = new HashMap<>();
    
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("job started");
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            em.createQuery("select m.team1,count(*) from MatchTeam m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(), team));

            em.createQuery("select m.team2,count(*) from MatchTeam m group by m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
                    });
            
            em.createQuery("select m.winner,count(*) from MatchTeam m group by m.winner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamData.get((String) e[0]);
                        if (team != null)
                            team.setTotalWins((long) e[1]);
                    });
            
            teamData.values().forEach(team -> em.merge(team));
            em.flush();
            em.clear();
            
            
            
            teamData.values().forEach(team -> System.out.println(team));
            

        }
    }

   
}
