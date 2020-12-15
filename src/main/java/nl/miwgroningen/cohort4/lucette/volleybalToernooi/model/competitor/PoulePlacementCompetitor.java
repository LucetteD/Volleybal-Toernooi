package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class PoulePlacementCompetitor extends Competitor {

    @ManyToOne
    Poule poule;

    Integer placement;

    @Override
    public Team getTeam() {
        if (!poule.finished()) {
            return null;
        } else {
            return poule.getPlace(placement);
        }
    }

    @Override
    public String toString() {
        Team team = getTeam();
        if (team == null) {
            return String.format("Place %d from Poule %s", placement, poule.getPouleName());
        } else {
            return team.getTeamName();
        }
    }
}
