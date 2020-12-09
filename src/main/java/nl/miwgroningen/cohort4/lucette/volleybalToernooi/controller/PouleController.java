package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Game;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.GameRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Controller
public class PouleController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PouleRepository pouleRepository;

    @Autowired
    GameRepository gameRepository;

    @GetMapping("/poules")
    protected String showPoules(Model model) {
        model.addAttribute("allPoules", pouleRepository.findAll());
        return "pouleOverview";
    }

    @GetMapping("/poules/{pouleName}")
    protected String showPouleDetails(@PathVariable("pouleName") String pouleName, Model model) {
        Optional<Poule> pouleOptional = pouleRepository.findByPouleName(pouleName);
        if (pouleOptional.isPresent()) {
            model.addAttribute("poule", pouleOptional.get());
            return "pouleDetails";
        } else {
            return "redirect:/poules";
        }
    }

    @GetMapping("/poules/generate")
    protected String showPouleGeneration(Model model) {
        int numberOfTeams = teamRepository.findAll().size();
        int numberOfPoules = numberOfPoules(numberOfTeams);
        int levelOfFinals = levelsOfFinals(numberOfTeams);
        int numberOfPouleGames = numberOfPouleGames(numberOfTeams, numberOfPoules);
        int numberOfFinalGames = numberOfFinalGames(levelOfFinals);

        model.addAttribute("numberOfPoules", numberOfPoules);
        model.addAttribute("levelsOfFinals", levelOfFinals);
        model.addAttribute("pouleGames", numberOfPouleGames);
        model.addAttribute("finalGames", numberOfFinalGames);
        return "pouleGeneration";
    }

    // XXX make this a nicer link
    @GetMapping("/poules/generate/no-really")
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

        int numberOfPoules = numberOfPoules(teams.size());
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



    // TODO: Should this functions be moved to "Poule"
    private int numberOfFinalGames(int levelOfFinals) {
        int noGames = 1;
        int noGamesAtLevel = 1;
        for (int level = 1; level < levelOfFinals; level++) {
            noGamesAtLevel *= 2;
            noGames += noGamesAtLevel;
        }
        return noGames;
    }

    private int numberOfPouleGames(int numberOfTeams, int numberOfPoules) {
        if (numberOfPoules <= 0) {
            return 0;
        }
        int pouleSize = numberOfTeams / numberOfPoules;
        int overSize = numberOfTeams % numberOfPoules;
        return numberOfPouleGames(pouleSize) * (numberOfPoules - overSize)
                + numberOfPouleGames(pouleSize + 1) * overSize;
    }

    private int numberOfPouleGames(int numberOfTeams) {
        int numberOfGames = 0;
        for (int i = 1; i < numberOfTeams; i++) {
            numberOfGames += i;
        }
        return numberOfGames;
    }

    private int numberOfPoules(int numberOfTeams) {
        if (numberOfTeams < 12) {
            return 0;
        } else {
            int maxPouleSize = 6;
            if (numberOfTeams % 6 <= 4) {
                maxPouleSize = 5;
            }
            return (int) Math.ceil(numberOfTeams / (double) maxPouleSize);
        }
    }

    private int levelsOfFinals(int numberOfTeams) {
        int finalsLevels = 1;
        int placesInFinals = 2;
        while(placesInFinals < numberOfTeams) {
            finalsLevels++;
            placesInFinals *= 2;
        }

        if (numberOfTeams < 12) { // XXX magic number
            return finalsLevels;
        } else {
            return finalsLevels - 1; // XXX magic number
        }
    }

}
