package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Poule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pouleId;

    private String pouleName;

    @OneToMany(mappedBy = "poule")
    private List<Team> myTeams;

    @OneToMany(mappedBy = "poule", fetch = FetchType.EAGER)
    private List<PouleGame> pouleGames;

    public Poule(){
        myTeams = new ArrayList<>();
        pouleGames = new ArrayList<>();
    }

    public List<Game> generatePouleGames(LocalDate startDate, LocalTime startTime, LocalTime endTime,
                                         int gameLength, int numberOfCourts, List<Game> games){
        List<Game> pouleGames = new ArrayList<>();
        System.out.println(this.getMyTeams());
        List<Team> teams = new ArrayList<>(this.getMyTeams());
        Team teamA = teams.remove(0);

        while (!teams.isEmpty()) {
            for (Team teamB : teams) {
                PouleGame game = new PouleGame();
                game.setHomeTeam(teamA);
                game.setVisitorTeam(teamB);
                game.setPoule(this);
                game.findSlot(startDate, startTime, endTime, gameLength, numberOfCourts, games);
                pouleGames.add(game);
                games.add(game);
            }
            teamA = teams.remove(0);
        }

        return pouleGames;
    }

    public Integer getPouleId() {
        return pouleId;
    }

    public void setPouleId(Integer pouleId) {
        this.pouleId = pouleId;
    }

    public String getPouleName() {
        return pouleName;
    }

    public void setPouleName(String naam) {
        this.pouleName = naam;
    }

    public int getNumberOfTeams() {
        return myTeams.size();
    }

    public List<Team> getMyTeams() {
        return myTeams;
    }

    public List<PouleGame> getPouleGames() {
        Collections.sort(pouleGames);
        return pouleGames;
    }

    public void addTeam(Team team) {
        myTeams.add(team);
    }

    public void addGame(PouleGame game) {
        pouleGames.add(game);
    }

    public boolean finished() {
        // TODO indicate if all games in the poule have been played
        return false;
    }

    public Team getPlace(Integer placement) {
        // TODO determine "winning" order in the poule and return the Team in "placement" position
        return null;
    }
}
