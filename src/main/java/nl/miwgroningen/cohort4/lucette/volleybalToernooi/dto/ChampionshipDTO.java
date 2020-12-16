package nl.miwgroningen.cohort4.lucette.volleybalToernooi.dto;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.FinalGame;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.PouleGame;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 *
 * Specify details about the tournament for planning the games
 */
public class ChampionshipDTO {

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate = LocalDate.of(2020, 12, 27);

    @DateTimeFormat(pattern="HH:mm")
    private LocalTime startTime = LocalTime.of(9, 30);

    @DateTimeFormat(pattern="HH:mm")
    private LocalTime endTime = LocalTime.of(19, 30);

    @Min(0)     // Game length should at least be positive
    @Max(150)   // 2,5 hours seems like enough
    private int gameLength = 60;

    @Min(0)
    private int numberOfCourts = 6;

    private int numberOfPoules;
    private int levelsOfFinals;
    private int numberOfPouleGames;
    private int numberOfFinalGames;

    public ChampionshipDTO() {
    }

    public ChampionshipDTO(int numberOfTeams) {
        numberOfPoules = PouleGame.numberOfPoules(numberOfTeams);
        levelsOfFinals = FinalGame.levelsOfFinals(numberOfTeams);
        numberOfPouleGames = PouleGame.numberOfPouleGames(numberOfTeams, numberOfPoules);
        numberOfFinalGames = FinalGame.numberOfFinalGames(levelsOfFinals);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
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
