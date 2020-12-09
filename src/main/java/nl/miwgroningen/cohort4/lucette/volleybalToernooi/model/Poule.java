package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "poule", cascade = CascadeType.DETACH)
    private List<Team> myTeams;

    @OneToMany(mappedBy = "poule")
    private List<PouleGame> pouleGames;

    public List<Game> generatePouleGames(){
        List<Game> pouleGames = new ArrayList<>();
        System.out.println(myTeams);
        List<Team> teams = new ArrayList<>(myTeams);
        Team teamA = teams.remove(0);

        while (!teams.isEmpty()) {
            for (Team teamB : teams) {
                PouleGame game = new PouleGame();
                game.setHomeTeam(teamA);
                game.setVisitorTeam(teamB);
                game.setPoule(this);
                pouleGames.add(game);
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
        return pouleGames;
    }
}
