package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
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

    // TODO maybe this can/should be done as one variable
    @OneToMany(mappedBy = "homeTeam")
    private List<Game> homeGames;

    @OneToMany(mappedBy = "visitorTeam")
    private List<Game> visitingGames;

    public int getNrOfPlayers() {
        return players.size();
    }

    public int getNrOfGames() {
        return homeGames.size() + visitingGames.size();
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
