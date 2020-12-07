package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PoolRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 * Stuur de views voor Team aan
 */

@Controller
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PoolRepository poolRepository;

    @GetMapping({"/", "/teams"})
    protected String showTeams(Model model) {
        model.addAttribute("allTeams", teamRepository.findAll());
        return "teamOverview";
    }

    @GetMapping("/team/{teamNaam}")
    protected String showTeamDetails(Model model, @PathVariable("teamNaam") String teamNaam) {
        Optional<Team> teamBox = teamRepository.findByTeamNaam(teamNaam);
        if (teamBox.isEmpty()) {
            return "redirect:/teams";
        }
        model.addAttribute("team", teamBox.get());
        return "teamDetails";
    }

    @GetMapping("/teams/add")
    protected String showTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("allPools", poolRepository.findAll());
        return "teamForm";
    }

    @PostMapping("/teams/add")
    protected String saveOrUpdateTeam(@ModelAttribute("team") Team team, BindingResult result) {
        if (result.hasErrors()) {
            return "teamForm";
        } else {
            teamRepository.save(team);
            return "redirect:/teams";
        }
    }

}
