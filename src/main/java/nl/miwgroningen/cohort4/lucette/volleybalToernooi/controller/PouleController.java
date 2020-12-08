package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
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

}
