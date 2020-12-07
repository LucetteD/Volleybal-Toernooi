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

    private String teamNaam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "poolId", referencedColumnName = "poolId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Pool pool;

    @OneToMany(mappedBy = "team")
    private List<Speler> spelers;

    public int getNrOfSpelers() {
        return spelers.size();
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamNaam() {
        return teamNaam;
    }

    public void setTeamNaam(String teamNaam) {
        this.teamNaam = teamNaam;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }
}
