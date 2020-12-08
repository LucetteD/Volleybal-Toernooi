package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer playerId;

    @NotBlank(message = "Voornaam is verplicht")
    private String firstName;

    @NotBlank(message = "Achternaam is verplicht")
    private String lastName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthdate;

    @Min(150)
    @Max(250)
    private int height = 175;

    @NotBlank()
    private String currentClub;

    @NotBlank()
    private String associationRegistrationNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teamId", referencedColumnName = "teamId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team team;

    @ManyToOne
    private Role role;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAssociationRegistrationNumber() {
        return associationRegistrationNumber;
    }

    public void setAssociationRegistrationNumber(String associationRegistrationNumber) {
        this.associationRegistrationNumber = associationRegistrationNumber;
    }
}
