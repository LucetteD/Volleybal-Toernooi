package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;

import javax.persistence.*;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class TeamCompetitor extends Competitor {

    @ManyToOne(cascade = CascadeType.DETACH)
    Team team;

    public TeamCompetitor() {
    }

    public TeamCompetitor(Team team) {
        this.team = team;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return team.getTeamName();
    }
}
