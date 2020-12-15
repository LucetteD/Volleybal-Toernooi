package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.FinalGame;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Team;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class FinalPlacementCompetitor extends Competitor {

    @ManyToOne
    FinalGame finalGame;

    Integer placement;

    public Team getTeam() {
        if (finalGame.getResult() == 0) {
            return null;
        } else if(finalGame.getResult() == placement) {
            // if result is 1 and placement is 1 then the winner of the game should be returned which is the home team
            // result is 2 and placement is 2 than the loser of the game shouhld be returned which is the home team
            return finalGame.getHomeTeam();
        } else {
            return finalGame.getVisitorTeam();
        }
    }

    @Override
    public String toString() {
        Team team = getTeam();
        if (team == null) {
            return String.format("%s of %s", placement == 1 ? "Winner" : "Loser", finalGame);
        } else {
            return team.getTeamName();
        }
    }
}
