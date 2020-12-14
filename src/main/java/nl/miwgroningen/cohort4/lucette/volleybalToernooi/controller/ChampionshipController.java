package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.dto.ChampionshipDTO;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Game;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.PouleGame;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.GameRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Controller
public class ChampionshipController {

    private final TeamRepository teamRepository;
    private final PouleRepository pouleRepository;
    private final GameRepository gameRepository;

    @Autowired
    public ChampionshipController(TeamRepository teamRepository, PouleRepository pouleRepository, GameRepository gameRepository) {
        this.teamRepository = teamRepository;
        this.pouleRepository = pouleRepository;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/championship")
    public String showChampionshipDetails(Model model) {
        int numberOfTeams = teamRepository.findAll().size();
        model.addAttribute("championshipDto", new ChampionshipDTO(numberOfTeams));
        return "pouleGeneration";
    }

    @GetMapping("/championship/generate")
    protected String generatePoules() {
        // remove all existing poules
        for (Team team : teamRepository.findAll()) {
            team.setPoule(null);
            teamRepository.saveAndFlush(team);
        }
        gameRepository.deleteAll();
        pouleRepository.deleteAll();

        List<Team> teams = teamRepository.findAll();

        // Make sure the poules are randomized
        Collections.shuffle(teams);

        int numberOfPoules = PouleGame.numberOfPoules(teams.size());
        int pouleSize = teams.size() / numberOfPoules;
        int overSize = teams.size() % numberOfPoules;

        // create poules and divide the teams among them
        for (int poule = 0; poule < numberOfPoules; poule++) {
            String pouleName = "" + (char) (poule + 65);
            Poule newPoule = new Poule();
            newPoule.setPouleName(pouleName);
            pouleRepository.save(newPoule);
            for (int pouleTeam = 0; pouleTeam < pouleSize; pouleTeam++) {
                // Added the minimum number of teams to this poule
                Team team = teams.remove(0);
                team.setPoule(newPoule);
                newPoule.addTeam(team);
                teamRepository.save(team);
            }
            if (poule < overSize) {
                // check if this is one of the "oversized poules" and add an additional team if it is.
                Team team = teams.remove(0);
                team.setPoule(newPoule);
                newPoule.addTeam(team);
                teamRepository.save(team);
            }
        }

        // generate pool games
        for (Poule poule : pouleRepository.findAll()) {
            List<Game> pouleGames = poule.generatePouleGames();
            gameRepository.saveAll(pouleGames);
        }

        // generate final games

        return "redirect:/poules";
    }
}
