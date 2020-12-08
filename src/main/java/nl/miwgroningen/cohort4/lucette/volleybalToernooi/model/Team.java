package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

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

    private String nationality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pouleId", referencedColumnName = "pouleId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Poule poule = null;

    @OneToMany(mappedBy = "team")
    private List<Player> players;

    @OneToMany(mappedBy = "team")
    private List<Game> games;

    public int getNrOfPlayers() {
        return players.size();
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
