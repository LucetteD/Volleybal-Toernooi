package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
@Entity
public class PouleGame extends Game {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pouleId", referencedColumnName = "pouleId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Poule poule;

    public Poule getPoule() {
        return poule;
    }

    public void setPoule(Poule poule) {
        this.poule = poule;
    }

    public static int numberOfPouleGames(int numberOfTeams, int numberOfPoules) {
        if (numberOfPoules <= 0) {
            return 0;
        }
        int pouleSize = numberOfTeams / numberOfPoules;
        int overSize = numberOfTeams % numberOfPoules;
        return numberOfPouleGames(pouleSize) * (numberOfPoules - overSize)
                + numberOfPouleGames(pouleSize + 1) * overSize;
    }

    private static int numberOfPouleGames(int numberOfTeams) {
        int numberOfGames = 0;
        for (int i = 1; i < numberOfTeams; i++) {
            numberOfGames += i;
        }
        return numberOfGames;
    }

    public static int numberOfPoules(int numberOfTeams) {
        if (numberOfTeams < 12) {
            return 0;
        } else {
            int maxPouleSize = 6;
            if (numberOfTeams % 6 <= 4) {
                maxPouleSize = 5;
            }
            return (int) Math.ceil(numberOfTeams / (double) maxPouleSize);
        }
    }
}
