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

    private String name;

    @OneToMany(mappedBy = "poule")
    private List<Team> myTeams;

    public Integer getPouleId() {
        return pouleId;
    }

    public void setPouleId(Integer pouleId) {
        this.pouleId = pouleId;
    }

    public String getNaam() {
        return name;
    }

    public void setNaam(String naam) {
        this.name = name;
    }

    public int getNumberOfTeams() {
        return myTeams.size();
    }

}
