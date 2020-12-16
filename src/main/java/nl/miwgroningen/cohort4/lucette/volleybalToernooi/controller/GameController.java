package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Game;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.PouleGame;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.FinalGameRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.GameRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Controller
public class GameController {

    private final GameRepository gameRepository;
    private final FinalGameRepository finalGameRepository;
    private final PouleRepository pouleRepository;

    @Autowired
    public GameController(GameRepository gameRepository, FinalGameRepository finalGameRepository, PouleRepository pouleRepository) {
        this.gameRepository = gameRepository;
        this.finalGameRepository = finalGameRepository;
        this.pouleRepository = pouleRepository;
    }

    @GetMapping({"/games"})
    protected String showPoules(Model model) {
        model.addAttribute("allPoules", pouleRepository.findAll());
        model.addAttribute("allFinalGames", finalGameRepository.findAll());
        return "gameOverview";
    }

    @GetMapping({"/games/overview"})
    protected String showPouleGames(Model model) {
        model.addAttribute("allPoules", pouleRepository.findAll());
        for (Poule poule : pouleRepository.findAll()) {
            model.addAttribute("allGames", gameRepository.findAll());
        }
        return "gameOverview";
    }
}
