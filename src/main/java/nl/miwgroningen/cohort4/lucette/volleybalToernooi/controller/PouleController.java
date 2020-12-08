package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Controller
public class PouleController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PouleRepository pouleRepository;

    @GetMapping("/pools")
    protected String showPools(Model model) {
        model.addAttribute("allPools", pouleRepository.findAll());
        model.addAttribute("pool", new Poule());
        return "poolOverzicht";
    }

    @PostMapping("/pools/add")
    protected String saveOrUpdatePool(@ModelAttribute("pool") Poule poule, BindingResult result) {
        if (result.hasErrors()) {
            return "pouleOverview";
        } else {
            pouleRepository.save(poule);
            return "redirect:/pools";
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
