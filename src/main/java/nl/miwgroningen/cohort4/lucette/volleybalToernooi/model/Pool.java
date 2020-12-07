package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer poolId;

    private String naam;

    @OneToMany(mappedBy = "pool")
    private List<Team> myTeams;

    public Integer getPoolId() {
        return poolId;
    }

    public void setPoolId(Integer poolId) {
        this.poolId = poolId;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getNumberOfTeams() {
        return myTeams.size();
    }

}
