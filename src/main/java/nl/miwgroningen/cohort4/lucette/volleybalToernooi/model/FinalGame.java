package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.FinalPlacementCompetitor;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.PoulePlacementCompetitor;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.WildCardCompetitor;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */

@Entity
public class FinalGame extends Game {

    private Integer finalLevel;

    public int getFinalLevel() {
        return finalLevel;
    }

    public void setFinalLevel(int finalLevel) {
        this.finalLevel = finalLevel;
    }

    @Override
    public String toString() {
        return String.format("1/%d Final %d", (int) Math.pow(2, finalLevel -1), this.getGameId());
    }

    public static List<Game> generateFinalGames(LocalDate startDate, LocalTime startTime, LocalTime endTime,
                                                int gameLength, int numberOfCourts,
                                                List<Poule> poules, int levelsOfFinals, List<Game> games) {
        List<Game> finalGames = new ArrayList<>();
        List<FinalGame> finalLevelGames = new ArrayList<>();

        // Generate the first level of finals (this one is special)
        int numberOfGames = (int) Math.pow(2, levelsOfFinals - 1);
        int wildCardGames = (numberOfGames - poules.size()) * 2;
        int regularGames = numberOfGames - wildCardGames / 2;

        startDate = Game.getLast(games).getTime().toLocalDate();
        LocalTime gameTime = Game.getLast(games).getTime().toLocalTime().plusMinutes(gameLength * 2);
        if (gameTime.isAfter(endTime)) {
            gameTime = startTime;
            startDate = startDate.plusDays(1);
        }
        // regular games (i.e. Poule A winner against Poule B runner-up)
        for (int i = 0; i < regularGames; i++) {
            FinalGame finalGame = new FinalGame();
            finalGame.setHomeCompetitor(
                    new PoulePlacementCompetitor(poules.get(i % poules.size()), 1));
            finalGame.setVisitorCompetitor(
                    new PoulePlacementCompetitor(poules.get((i + 1) % poules.size()), 2));
            finalGame.setFinalLevel(levelsOfFinals);
            finalGame.findSlot(startDate, startTime, endTime, gameTime, gameLength, numberOfCourts, games);
            finalLevelGames.add(finalGame);
            games.add(finalGame);
        }
        // wildcard games (i.e. remaining winners and runners-up against the numbers 3)
        for (int i = regularGames; i < numberOfGames; i++) {
            FinalGame finalGame = new FinalGame();
            finalGame.setHomeCompetitor(
                    new PoulePlacementCompetitor(poules.get(i % poules.size()), i / poules.size() + 1));
            finalGame.setVisitorCompetitor(
                    new WildCardCompetitor(3, wildCardGames--)); // TODO not sure if this is ALWAYS 3
            finalGame.setFinalLevel(levelsOfFinals);
            finalGame.findSlot(startDate, startTime, endTime, gameTime, gameLength, numberOfCourts, games);
            finalLevelGames.add(finalGame);
            games.add(finalGame);
        }
        System.err.println("Generated " + finalLevelGames.size() + " games");
        finalGames.addAll(finalLevelGames);
        levelsOfFinals--;

        List<FinalGame> newFinalLevelGames = new ArrayList<>();
        for (;levelsOfFinals > 0; levelsOfFinals--) {
            newFinalLevelGames.clear();
            startDate = Game.getLast(finalLevelGames).getTime().toLocalDate();
            gameTime = Game.getLast(finalLevelGames).getTime().toLocalTime().plusMinutes(gameLength * 2);
            if (gameTime.isAfter(endTime)) {
                gameTime = startTime;
                startDate = startDate.plusDays(1);
            }
            for (int i = 0; i < Math.pow(2, levelsOfFinals - 1); i++) {
                FinalGame finalGame = new FinalGame();
                finalGame.setHomeCompetitor(new FinalPlacementCompetitor(finalLevelGames.get(i * 2), 1));
                finalGame.setVisitorCompetitor(new FinalPlacementCompetitor(finalLevelGames.get((i * 2) + 1), 1));
                finalGame.setFinalLevel(levelsOfFinals);
                finalGame.findSlot(startDate, startTime, endTime, gameTime, gameLength, numberOfCourts, games);
                newFinalLevelGames.add(finalGame);
                games.add(finalGame);
            }
            if (levelsOfFinals == 1) {
                // also generate 3rd/4th position finals
                FinalGame finalGame = new FinalGame();
                finalGame.setHomeCompetitor(new FinalPlacementCompetitor(finalLevelGames.get(0), 2));
                finalGame.setVisitorCompetitor(new FinalPlacementCompetitor(finalLevelGames.get(1), 2));
                finalGame.setFinalLevel(levelsOfFinals);
                finalGame.findSlot(startDate, startTime, endTime, gameTime, gameLength, numberOfCourts, games);
                newFinalLevelGames.add(finalGame);
                games.add(finalGame);
            }
            finalLevelGames.clear();
            finalLevelGames.addAll(newFinalLevelGames);
            finalGames.addAll(finalLevelGames);
        }

        return finalGames;
    }

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
