package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Role;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PlayerRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Controller
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PlayerRepository playerRepository;

//    @GetMapping("/game/add/{playerId}")
//    protected String addGame(@PathVariable("playerId") Integer teamId) {
//        Optional<Player> playerBox = playerRepository.findById(playerId);
//        if (playerBox.isPresent()) {
//            Game game = new Game();
//            game.setPlayer(playerBox.get());
//            playerRepository.save(player);
//        }
//        return "redirect:/players"; // TODO Add aantal gespeelde wedstrijden in playerOverview
//    }

    @GetMapping("role/add")
    protected String showRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "roleForm";
    }

    @PostMapping("role/add")
    protected String saveOrUpdateRole(@ModelAttribute("role") Role role, BindingResult result) {
        // TODO check if role already exists
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            result.addError(new ObjectError("roleName", "This role already exists"));
        }

        if (result.hasErrors()) {
            return "roleForm";
        } else {
            roleRepository.save(role);
            return "redirect:/teams";
        }
    }
}
