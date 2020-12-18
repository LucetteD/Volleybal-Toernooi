package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Player;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.TeamCompetitor;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.*;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.utility.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;
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

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping({"/", "/teams"})
    protected String showTeams(Model model) {
        model.addAttribute("allTeams", teamRepository.findAll());
        return "teamOverview";
    }

    @GetMapping("/teams/{teamName}")
    protected String showTeamDetails(Model model, @PathVariable("teamName") String teamName) {
        Optional<Team> teamBox = teamRepository.findByTeamName(teamName);
        if (teamBox.isEmpty()) {
            return "redirect:/teams";
        }
        model.addAttribute("team", teamBox.get());
        return "teamDetails";
    }

    @GetMapping("/teams/add")
    @Secured("ROLE_ADMIN")
    protected String showTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("allPoules", pouleRepository.findAll());
        return "teamForm";
    }

    @GetMapping("/teams/change/{teamName}")
    @Secured("ROLE_ADMIN")
    protected String changeTeam(@PathVariable("teamName") String teamName, Model model) {
        Optional<Team> teamOptional = teamRepository.findByTeamName(teamName);
        if (teamOptional.isEmpty()) {
            return "redirect:/teams";
        }
        model.addAttribute("team", teamOptional.get());
        model.addAttribute("allPlayers", playerRepository.findAll());
        model.addAttribute("allPoules", pouleRepository.findAll());
        return "teamForm";
    }

    @PostMapping("/teams/add")
    @Secured("ROLE_ADMIN")
    protected String saveOrUpdateTeam(@ModelAttribute("team") Team team, BindingResult result,
                                      @RequestParam("nationalFlagImage") MultipartFile nationalFlagImage)
            throws IOException {
        if (teamRepository.findByTeamName(team.getTeamName()).isPresent()) {
            // A team with this naam already exists
            result.rejectValue("teamName", "error.user", "Deze teamnaam is al in gebruik.");
        }

        String imageFileName = StringUtils.cleanPath(team.getTeamName() + "."
                + StringUtils.getFilenameExtension(nationalFlagImage.getOriginalFilename()));
        String uploadDirectory = "images\\team-images\\";
        team.setNationalFlag(uploadDirectory + imageFileName);
        FileUploadUtil.saveFile(uploadDirectory, imageFileName, nationalFlagImage);

        if (result.hasErrors()) {
            return "teamForm";
        } else {
            teamRepository.save(team);
            return "redirect:/teams";
        }
    }

}
