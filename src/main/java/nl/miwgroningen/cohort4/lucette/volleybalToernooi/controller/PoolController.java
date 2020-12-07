package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Pool;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PoolRepository;
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
public class PoolController {

    @Autowired
    PoolRepository poolRepository;

    @GetMapping("/pools")
    protected String showPools(Model model) {
        model.addAttribute("allPools", poolRepository.findAll());
        model.addAttribute("pool", new Pool());
        return "poolOverzicht";
    }

    @PostMapping("/pool/add")
    protected String saveOrUpdatePool(@ModelAttribute("pool") Pool pool, BindingResult result) {
        if (result.hasErrors()) {
            return "poolOverview";
        } else {
            poolRepository.save(pool);
            return "redirect:/pools";
        }
    }

}
