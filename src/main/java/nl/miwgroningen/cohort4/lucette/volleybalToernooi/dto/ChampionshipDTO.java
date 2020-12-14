package nl.miwgroningen.cohort4.lucette.volleybalToernooi.dto;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.FinaleGame;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.PouleGame;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 *
 * Specify details about the tournament for planning the games
 */
public class ChampionshipDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @Min(0)     // Game length should at least be positive
    @Max(150)   // 2,5 hours seems like enough
    private int gameLength;

    @Min(0)
    private int numberOfCourts;

    private int numberOfPoules;
    private int levelsOfFinals;
    private int numberOfPouleGames;
    private int numberOfFinalGames;

    public ChampionshipDTO() {
    }

    public ChampionshipDTO(int numberOfTeams) {
        numberOfPoules = PouleGame.numberOfPoules(numberOfTeams);
        levelsOfFinals = FinaleGame.levelsOfFinals(numberOfTeams);
        numberOfPouleGames = PouleGame.numberOfPouleGames(numberOfTeams, numberOfPoules);
        numberOfFinalGames = FinaleGame.numberOfFinalGames(levelsOfFinals);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getGameLength() {
        return gameLength;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    public int getNumberOfCourts() {
        return numberOfCourts;
    }

    public void setNumberOfCourts(int numberOfCourts) {
        this.numberOfCourts = numberOfCourts;
    }

    public int getNumberOfPoules() {
        return numberOfPoules;
    }

    public void setNumberOfPoules(int numberOfPoules) {
        this.numberOfPoules = numberOfPoules;
    }

    public int getLevelsOfFinals() {
        return levelsOfFinals;
    }

    public void setLevelsOfFinals(int levelsOfFinals) {
        this.levelsOfFinals = levelsOfFinals;
    }

    public int getNumberOfPouleGames() {
        return numberOfPouleGames;
    }

    public void setNumberOfPouleGames(int numberOfPouleGames) {
        this.numberOfPouleGames = numberOfPouleGames;
    }

    public int getNumberOfFinalGames() {
        return numberOfFinalGames;
    }

    public void setNumberOfFinalGames(int numberOfFinalGames) {
        this.numberOfFinalGames = numberOfFinalGames;
    }
}
