package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Player;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PlayerRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.RoleRepository;
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
 */

@Controller
public class PlayerController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping({"/", "/players"})
    protected String showTeams(Model model) {
        model.addAttribute("allPlayers", playerRepository.findAll());
        return "playerOverview";
    }

    @GetMapping("/game/add/{teamId}")
    protected String addGame(@PathVariable("teamId") Integer teamId) {
        Optional<Team> teamBox = teamRepository.findById(teamId);
        if (teamBox.isPresent()) {
            Player player = new Player();
            player.setTeam(teamBox.get());
            playerRepository.save(player);
        }
        return "redirect:/teams";
    }

    @GetMapping("/player/add/t/{teamName}")
    protected String addPlayerByName(@PathVariable("teamName") String teamName) {
        Optional<Team> teamBox = teamRepository.findByTeamName(teamName);
        if (teamBox.isPresent()) {
            Player player = new Player();
            player.setTeam(teamBox.get());
            playerRepository.save(player);
        }
        return "redirect:/teams";
    }

    @GetMapping("/players/add")
    protected String addNewPlayer(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("allTeams", teamRepository.findAll());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "playerForm";
    }

    @PostMapping("/players/add")
    protected String saveOrUpdatePlayer(@ModelAttribute("player") Player player, BindingResult result, Model model) {
        if (playerRepository.existsByAssociationRegistrationNumber(player.getAssociationRegistrationNumber())) {
            result.rejectValue("associationRegistrationNumber", "error.user",
                    "Een speler met dit bondsnummer bestaat al");
        }

        if (result.hasErrors()) {
            System.out.println(result);
            model.addAttribute("allTeams", teamRepository.findAll());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "playerForm";
        } else {
            playerRepository.save(player);
            return "redirect:/players";
        }
    }

}
