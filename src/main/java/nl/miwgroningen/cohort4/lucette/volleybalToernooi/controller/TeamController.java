package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    PouleRepository pouleRepository;

    @GetMapping({"/", "/teams"})
    protected String showTeams(Model model) {
        model.addAttribute("allTeams", teamRepository.findAll());
        return "teamOverview";
    }

    @GetMapping("/team/{teamName}")
    protected String showTeamDetails(Model model, @PathVariable("teamName") String teamName) {
        Optional<Team> teamBox = teamRepository.findByTeamName(teamName);
        if (teamBox.isEmpty()) {
            return "redirect:/teams";
        }
        model.addAttribute("team", teamBox.get());
        return "teamDetails";
    }

    @GetMapping("/teams/add")
    protected String showTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("allPools", pouleRepository.findAll());
        return "teamForm";
    }

    @PostMapping("/teams/add")
    protected String saveOrUpdateTeam(@ModelAttribute("team") Team team, BindingResult result) {
        if (teamRepository.findByTeamName(team.getTeamName()).isPresent()) {
            // A team with this naam already exists
            result.addError(new ObjectError("teamName", "Deze team naam is al in gebruik"));
        }
        if (result.hasErrors()) {
            System.out.println(result);
            return "teamForm";
        } else {
            teamRepository.save(team);
            return "redirect:/teams";
        }
    }

}
