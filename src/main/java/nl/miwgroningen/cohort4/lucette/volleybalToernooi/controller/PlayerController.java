package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Player;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PlayerRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

public class PlayerController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping("/copy/add/{teamId}")
    protected String addCopy(@PathVariable("teamId") Integer teamId) {
        Optional<Team> teamBox = teamRepository.findById(teamId);
        if (teamBox.isPresent()) {
            Player player = new Player();
            player.setTeam(teamBox.get());
            playerRepository.save(player);
        }
        return "redirect:/teams";
    }

    @GetMapping("/speler/add/t/{teamName}")
    protected String addSpelerByName(@PathVariable("teamName") String teamName) {
        Optional<Team> teamBox = teamRepository.findByTeamName(teamName);
        if (teamBox.isPresent()) {
            Player player = new Player();
            player.setTeam(teamBox.get());
            playerRepository.save(player);
        }
        return "redirect:/teams";
    }

}
