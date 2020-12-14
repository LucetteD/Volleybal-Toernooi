package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

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

}
