package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.utility.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    protected String saveOrUpdateTeam(@ModelAttribute("team") Team team, BindingResult result,
                                      @RequestParam("nationalFlagImage") MultipartFile nationalFlagImage)
            throws IOException {
        if (teamRepository.findByTeamName(team.getTeamName()).isPresent()) {
            // A team with this naam already exists
            result.rejectValue("teamName", "error.user", "Deze teamnaam is al in gebruik.");
        }

        String imageFileName = StringUtils.cleanPath(team.getTeamName() + "."
                + StringUtils.getFilenameExtension(nationalFlagImage.getOriginalFilename()));
        team.setNationalFlag(imageFileName);
        String uploadDirectory = "team-images/";
        FileUploadUtil.saveFile(uploadDirectory, imageFileName, nationalFlagImage);

        if (result.hasErrors()) {
            return "teamForm";
        } else {
            teamRepository.save(team);
            return "redirect:/teams";
        }
    }

}
