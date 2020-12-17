package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;

import javax.persistence.Entity;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class WildCardCompetitor extends Competitor {

    Integer placement;
    Integer secondaryPlacement;

    public WildCardCompetitor(int placement, int secondaryPlacement) {
        super();
        this.placement = placement;
        this.secondaryPlacement = secondaryPlacement;
    }

    public WildCardCompetitor() {
    }

    //TODO WildCardCompetitor should be selected based on overall qualifier in order of secondaryPlacement
    // i.e. WildCard
    @Override
    public Team getTeam() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("This will be the %dth number %d of all poules", secondaryPlacement, placement);
    }

    public Integer getSecondaryPlacement() {
        return secondaryPlacement;
    }

    public void setSecondaryPlacement(Integer secondaryPlacement) {
        this.secondaryPlacement = secondaryPlacement;
    }
}
