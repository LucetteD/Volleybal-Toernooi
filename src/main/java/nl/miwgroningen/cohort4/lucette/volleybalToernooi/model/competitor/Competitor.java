package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Entity
public abstract class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer competitorId;

    public abstract Team getTeam();

    public Integer getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(Integer competitorId) {
        this.competitorId = competitorId;
    }
}
