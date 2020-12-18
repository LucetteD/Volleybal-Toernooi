package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.Competitor;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.TeamCompetitor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 * Representatie van een team binnen het toernooi
 */

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer teamId;

    @Column(unique = true)
    private String teamName;

    private String nationalFlag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pouleId", referencedColumnName = "pouleId")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Poule poule;

    @OneToMany(mappedBy = "team")
    private List<Player> players;


    @OneToMany(mappedBy = "team")
    private List<TeamCompetitor> competitions;

    public int getNrOfPlayers() {
        return players.size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Game> getGames() {
        // TODO this is borked
        List<Game> games = new ArrayList<>();
        for (Competitor competition : competitions) {
            games.add(competition.getGame());
        }
        return games;
    }


    public int getNrOfGames() {
        return competitions.size();
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Poule getPoule() {
        return poule;
    }

    public void setPoule(Poule poule) {
        this.poule = poule;
    }

    public String getNationalFlag() {
        return nationalFlag;
    }

    public void setNationalFlag(String nationalFlag) {
        this.nationalFlag = nationalFlag;
    }
}
