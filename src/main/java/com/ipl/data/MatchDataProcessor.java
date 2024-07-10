package com.ipl.data;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ipl.model.MatchTeam;

public class MatchDataProcessor implements ItemProcessor<MatchInput, MatchTeam> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public MatchTeam process(MatchInput matchInput) throws Exception {
        MatchTeam match = new MatchTeam();
        match.setId(Long.parseLong(matchInput.getId()));
        match.setCity(matchInput.getCity());
        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setPlayer_of_match(matchInput.getPlayer_of_match());
        match.setVenue(matchInput.getVenue());
        match.setNeutral_venue(matchInput.getNeutral_venue());

        String firstInningsTeam, secondInningsTeam;

        if ("bat".equals(matchInput.getToss_decision())) {
            firstInningsTeam = matchInput.getToss_winner();
            secondInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ? matchInput.getTeam2()
                    : matchInput.getTeam1();
        } else {
            secondInningsTeam = matchInput.getToss_winner();
            firstInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ? matchInput.getTeam2()
                    : matchInput.getTeam1();
        }
        match.setTeam1(firstInningsTeam);
        match.setTeam2(secondInningsTeam);
        match.setToss_winner(matchInput.getToss_winner());
        match.setToss_decision(matchInput.getToss_decision());
        match.setWinner(matchInput.getWinner());
        match.setResult(matchInput.getResult());
        match.setResult_margin(matchInput.getResult_margin());
        match.setEliminator(matchInput.getEliminator());
        match.setMethod(matchInput.getMethod());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());

        return match;

    }

}
