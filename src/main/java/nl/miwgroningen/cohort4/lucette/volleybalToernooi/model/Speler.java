package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Speler {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer spelerId;

    private String firstName;

    private String lastName;

    private LocalDate birthdate;

    private double height;

    private String currentClub;

    private int shirtNumber;

    // Limit to dropdown menu
    private String position;

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
