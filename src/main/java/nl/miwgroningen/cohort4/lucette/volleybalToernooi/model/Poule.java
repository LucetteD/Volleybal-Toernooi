package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Poule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer pouleId;

    private String naam;

    @OneToMany(mappedBy = "poule")
    private List<Team> myTeams;

    public Integer getPouleId() {
        return pouleId;
    }

    public void setPouleId(Integer pouleId) {
        this.pouleId = pouleId;
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
