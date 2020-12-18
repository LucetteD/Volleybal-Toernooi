package nl.miwgroningen.cohort4.lucette.volleybalToernooi.controller;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.dto.ChampionshipDTO;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.*;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.Competitor;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.FinalPlacementCompetitor;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.CompetitorRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.GameRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.PouleRepository;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Controller
public class ChampionshipController {

    private final TeamRepository teamRepository;
    private final PouleRepository pouleRepository;
    private final GameRepository gameRepository;
    private final CompetitorRepository competitorRepository;

    @Autowired
    public ChampionshipController(TeamRepository teamRepository, PouleRepository pouleRepository, GameRepository gameRepository, CompetitorRepository competitorRepository) {
        this.teamRepository = teamRepository;
        this.pouleRepository = pouleRepository;
        this.gameRepository = gameRepository;
        this.competitorRepository = competitorRepository;
    }

    @GetMapping("/championship")
    public String showChampionshipDetails(Model model) {
        int numberOfTeams = teamRepository.findAll().size();
        model.addAttribute("championshipDto", new ChampionshipDTO(numberOfTeams));
        return "championshipGeneration";
    }

    @PostMapping("/championship/generate")
    @Secured("ROLE_ADMIN")
    protected String generatePoules(@ModelAttribute("championshipDto") @Valid ChampionshipDTO championshipDTO,
                                    BindingResult result) {
        // TODO should we store "old" championships?
        // remove all existing poules and games
        if (result.hasErrors()) {
            return "championshipGeneration";
        }

        for (Team team : teamRepository.findAll()) {
            team.setPoule(null);
            teamRepository.saveAndFlush(team);
        }

        for (Competitor competitor : competitorRepository.findAll()) {
            if (competitor instanceof FinalPlacementCompetitor) {
                ((FinalPlacementCompetitor) competitor).setFinalGame(null);
                competitorRepository.saveAndFlush(competitor);
            }
        }
        competitorRepository.deleteAll();
        competitorRepository.flush();
        gameRepository.deleteAll();
        gameRepository.flush();
        pouleRepository.deleteAll();
        pouleRepository.flush();

        List<Team> teams = teamRepository.findAll();

        // Make sure the poules are randomized
        Collections.shuffle(teams);

        int numberOfPoules = championshipDTO.getNumberOfPoules();
        int pouleSize = teams.size() / numberOfPoules;
        int overSize = teams.size() % numberOfPoules;

        // create poules and divide the teams among them
        for (int poule = 0; poule < numberOfPoules; poule++) {
            String pouleName = "" + (char) (poule + 65);
            Poule newPoule = new Poule();
            newPoule.setPouleName(pouleName);
            pouleRepository.save(newPoule);
            for (int pouleTeam = 0; pouleTeam < pouleSize; pouleTeam++) {
                // Added the minimum number of teams to this poule
                Team team = teams.remove(0);
                team.setPoule(newPoule);
                newPoule.addTeam(team);
                teamRepository.save(team);
            }
            if (poule < overSize) {
                // check if this is one of the "oversized poules" and add an additional team if it is.
                Team team = teams.remove(0);
                team.setPoule(newPoule);
                newPoule.addTeam(team);
                teamRepository.save(team);
            }
        }

        // generate pool games
        for (Poule poule : pouleRepository.findAll()) {
            List<Game> pouleGames = poule.generatePouleGames(
                    championshipDTO.getStartDate(),
                    championshipDTO.getStartTime(),
                    championshipDTO.getEndTime(),
                    championshipDTO.getGameLength(),
                    championshipDTO.getNumberOfCourts(),
                    gameRepository.findAll()
            );
            for (Game pouleGame : pouleGames) {
                // TODO I feel this must be done more efficiently using CASCADE
                competitorRepository.save(pouleGame.getHomeCompetitor());
                competitorRepository.save(pouleGame.getVisitorCompetitor());
            }
            gameRepository.saveAll(pouleGames);
            for (Game pouleGame : pouleGames) {
                // TODO I feel this must be done more efficiently using CASCADE
                pouleGame.getHomeCompetitor().setGame(pouleGame);
                competitorRepository.save(pouleGame.getHomeCompetitor());
                pouleGame.getVisitorCompetitor().setGame(pouleGame);
                competitorRepository.save(pouleGame.getVisitorCompetitor());
            }
        }

        List<Game> finalGames = FinalGame.generateFinalGames(
                championshipDTO.getStartDate(),
                championshipDTO.getStartTime(),
                championshipDTO.getEndTime(),
                championshipDTO.getGameLength(),
                championshipDTO.getNumberOfCourts(),
                pouleRepository.findAll(),
                championshipDTO.getLevelsOfFinals(),
                gameRepository.findAll()
        );
        for (Game finalGame : finalGames) {
            // TODO I feel this might be done more efficiently using CASCADE
            competitorRepository.save(finalGame.getHomeCompetitor());
            competitorRepository.save(finalGame.getVisitorCompetitor());
            gameRepository.save(finalGame);
        }
        //gameRepository.saveAll(finalGames);
        // TODO generate final games

        return "redirect:/poules";
    }
}
