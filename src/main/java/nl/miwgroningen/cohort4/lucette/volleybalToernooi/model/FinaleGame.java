package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import javax.persistence.Entity;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Entity
public class FinaleGame extends Game {

    public static int numberOfFinalGames(int levelOfFinals) {
        int noGames = 1;
        int noGamesAtLevel = 1;
        for (int level = 1; level < levelOfFinals; level++) {
            noGamesAtLevel *= 2;
            noGames += noGamesAtLevel;
        }
        return noGames;
    }

    public static int levelsOfFinals(int numberOfTeams) {
        int finalsLevels = 1;
        int placesInFinals = 2;
        while(placesInFinals < numberOfTeams) {
            finalsLevels++;
            placesInFinals *= 2;
        }

        if (numberOfTeams < 12) { // XXX magic number
            return finalsLevels;
        } else {
            return finalsLevels - 1; // XXX magic number
        }
    }
}
