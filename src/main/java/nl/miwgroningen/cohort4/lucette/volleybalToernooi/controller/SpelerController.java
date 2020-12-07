package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Speler;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.SpelerRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */
public class SpelerController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    SpelerRepository spelerRepository;

    @GetMapping("/copy/add/{teamId}")
    protected String addCopy(@PathVariable("teamId") Integer teamId) {
        Optional<Team> teamBox = teamRepository.findById(teamId);
        if (teamBox.isPresent()) {
            Speler speler = new Speler();
            speler.setTeam(teamBox.get());
            spelerRepository.save(speler);
        }
        return "redirect:/teams";
    }

    @GetMapping("/speler/add/t/{teamNaam}")
    protected String addSpelerByName(@PathVariable("teamNaam") String teamNaam) {
        Optional<Team> teamBox = teamRepository.findByTeamNaam(teamNaam);
        if (teamBox.isPresent()) {
            Speler speler = new Speler();
            speler.setTeam(teamBox.get());
            spelerRepository.save(speler);
        }
        return "redirect:/teams";
    }

}
