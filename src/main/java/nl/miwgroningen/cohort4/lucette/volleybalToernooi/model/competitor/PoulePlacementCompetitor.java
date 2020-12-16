package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Poule;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class PoulePlacementCompetitor extends Competitor {

    @ManyToOne(cascade = CascadeType.DETACH)
    Poule poule = null;

    Integer placement;

    public PoulePlacementCompetitor(Poule poule, int placement) {
        super();
        this.poule = poule;
        this.placement = placement;
    }

    public PoulePlacementCompetitor(){}

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
