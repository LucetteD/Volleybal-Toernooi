package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Game;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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
    public GameController(GameRepository gameRepository, FinalGameRepository finalGameRepository,
                          PouleRepository pouleRepository) {
        this.gameRepository = gameRepository;
        this.finalGameRepository = finalGameRepository;
        this.pouleRepository = pouleRepository;
    }

    @Autowired
    TeamRepository teamRepository;

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

    @GetMapping("/games/{gameId}")
    protected String showGameDetails(@PathVariable("gameId") Integer gameId, Model model) {
        Optional<Game> game = gameRepository.findByGameId(gameId);
        if (game.isEmpty()) {
            return showPouleGames(model);
        } else {
            model.addAttribute("game", game.get());
            return "gameDetails";
        }
    }

    @GetMapping("/games/details/add/{gameId}")
    @Secured("ROLE_ADMIN")
    protected String addDetailsGame(@PathVariable("gameId") Integer gameId, Model model) {
        Optional<Game> gameOptional = gameRepository.findByGameId(gameId);
        if (gameOptional.isEmpty()) {
            return "redirect:/games";
        }
        model.addAttribute("game", gameOptional.get());
        model.addAttribute("allGames", gameRepository.findAll());
        return "gameForm";
    }

    @PostMapping("/games/newDetails/add")
    @Secured("ROLE_ADMIN")
    protected String saveAndUpdateResult(@ModelAttribute("game") @Valid Game game, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result);
            model.addAttribute("allGames", gameRepository.findAll());
            model.addAttribute("gameResult", gameRepository.findAll());
            model.addAttribute("result", gameRepository.findAll());
            return "gameForm";
        } else {
            gameRepository.save(game);
            return "redirect:/gameDetails";
        }
    }
}
