package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Speler {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer spelerId;

    private Boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teamId", referencedColumnName = "teamId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team team;

    public Integer getSpelerId() {
        return spelerId;
    }

    public void setSpelerId(Integer spelerId) {
        this.spelerId = spelerId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
