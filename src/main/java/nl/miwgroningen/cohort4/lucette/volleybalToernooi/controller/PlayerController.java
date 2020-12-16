package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Player;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PlayerRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.RoleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
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
import java.util.List;
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

    @GetMapping({"/players"})
    protected String showPlayers(Model model) {
        model.addAttribute("allPlayers", playerRepository.findAll());
        return "playerOverview";
    }

    @GetMapping("/players/{lastName}")
    protected String showPlayerDetails(@PathVariable("lastName") String lastName, Model model) {
        List<Player> players = playerRepository.findPlayersByLastName(lastName);

        if (players.size() == 0) {
            // No players with this lastName
            return showPlayers(model);
        } else if (players.size() == 1) {
            // Single player with this lastName show their details
            model.addAttribute("player", players.get(0));
            return "playerDetails";
        } else {
            // more than one player has this lastName, show disambiguation page
            model.addAttribute("allMatchingPlayers", players);
            return "playerDisambiguation";
        }
    }

    @GetMapping("/players/{lastName}/{playerId}")
    protected String showPlayerDetails(@PathVariable("lastName") String lastName,
                                       @PathVariable("playerId") Integer playerId,
                                       Model model) {
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isEmpty()) {
            return showPlayers(model);
        } else {
            model.addAttribute("player", player.get());
            return "playerDetails";
        }
    }

    @GetMapping("/players/add")
    @Secured("ROLE_ADMIN")
    protected String addNewPlayer(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("allTeams", teamRepository.findAll());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "playerForm";
    }

    @GetMapping("/players/change/{playerId}")
    @Secured("ROLE_ADMIN")
    protected String changePlayer(@PathVariable("playerId") Integer playerId, Model model) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        if (playerOptional.isEmpty()) {
            return "redirect:/players";
        }
        model.addAttribute("player", playerOptional.get());
        model.addAttribute("allTeams", teamRepository.findAll());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "playerForm";
    }

    @PostMapping("/players/add")
    @Secured("ROLE_ADMIN")
    protected String saveOrUpdatePlayer(@ModelAttribute("player") @Valid Player player, BindingResult result, Model model) {
        Optional<Player> existingPlayer =
                playerRepository.findByAssociationRegistrationNumber(player.getAssociationRegistrationNumber());
        if (existingPlayer.isPresent() && !existingPlayer.get().getPlayerId().equals(player.getPlayerId())) {
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
