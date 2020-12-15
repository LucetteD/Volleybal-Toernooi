package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;

import javax.persistence.*;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class TeamCompetitor extends Competitor {

    @ManyToOne
    Team team;

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return team.getTeamName();
    }
}
