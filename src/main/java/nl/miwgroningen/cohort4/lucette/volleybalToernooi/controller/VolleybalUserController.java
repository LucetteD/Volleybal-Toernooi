package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.VolleybalUser;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.VolleybalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class VolleybalUserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    VolleybalUserRepository volleybalUserRepository;

    @GetMapping("/user/new")
    @Secured("ROLE_ADMIN")
    protected String showNewUserForm(Model model) {
        model.addAttribute("user", new VolleybalUser());
        model.addAttribute("allUsers", volleybalUserRepository.findAll());
        return "userForm";
    }

    @PostMapping("/user/new")
    @Secured("ROLE_ADMIN")
    protected String saveOrUpdateUser(@ModelAttribute("user") VolleybalUser user, BindingResult result) {
        if (result.hasErrors()) {
            return "userForm";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            volleybalUserRepository.save(user);
            return "redirect:/";

        }
    }

}
