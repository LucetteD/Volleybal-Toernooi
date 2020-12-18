package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.Competitor;
import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.TeamCompetitor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Lucette Das <l.k.das@st.hanze.nl>
 */

@Entity
public class Game implements Comparable<Game> {

    private final String NOG_GEEN_UITSLAG_BEKEND = "Nog niet bekend";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gameId;

    private LocalDateTime time;

    private String location;

    private Integer setPointsHomeSetOne;
    private Integer setPointsVisitorSetOne;
    private Integer setPointsHomeSetTwo;
    private Integer setPointsVisitorSetTwo;
    private Integer setPointsHomeSetThree;
    private Integer setPointsVisitorSetThree;

    // 0 = no winner/not yet played, 1 = home team won, 2 = visitor team won
    private int result = 0;

    private String gameResult;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "homeTeamId", referencedColumnName = "competitorId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competitor homeCompetitor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visitorTeamId", referencedColumnName = "competitorId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competitor visitorCompetitor;

    public void findSlot(LocalDate gameDate, LocalTime startTime, LocalTime endTime,
                         int gameLength, int numberOfCourts, List<Game> games) {
        findSlot(gameDate, startTime, endTime, startTime, gameLength, numberOfCourts, games);
    }

    public void findSlot(LocalDate gameDate, LocalTime startTime, LocalTime endTime, LocalTime gameTime,
                         int gameLength, int numberOfCourts, List<Game> games) {
        while ( !timeSlotAvailable(this.homeCompetitor, LocalDateTime.of(gameDate, gameTime), gameLength, games) ||
                !timeSlotAvailable(this.visitorCompetitor, LocalDateTime.of(gameDate, gameTime), gameLength, games) ||
                !findCourt(LocalDateTime.of(gameDate, gameTime), numberOfCourts, games)) {
            gameTime = gameTime.plusMinutes(gameLength);
            if (gameTime.isAfter(endTime)) {
                gameDate = gameDate.plusDays(1);
                gameTime = startTime;
            }
        }
    }

    private boolean timeSlotAvailable(Competitor competitor, LocalDateTime gameTime, int gameLength, List<Game> games) {
        final int MAX_CONSECUTIVE_GAMES = 2;
        int precedingGames = 0;

        if (!(competitor instanceof TeamCompetitor)) {
            // TODO availability for Schrodinger's competitors needs to be sorted out but might not be an issue
            return true;
        }
        for (Game game : games) {
            // if the competitor is in the game
            if (competitor.getTeam().equals(game.getHomeCompetitor().getTeam()) ||
                    competitor.getTeam().equals(game.getVisitorCompetitor().getTeam())) {
                if (game.time.isEqual(gameTime)) {
                    // and it is at the proposed time, that time is not available
                    return false;
                } else if (game.getTime().isAfter(gameTime.minusMinutes(MAX_CONSECUTIVE_GAMES * gameLength + 1))) {
                    // if it is within 2 game lengths before the proposed time, record it
                    precedingGames++;
                    if (precedingGames >= MAX_CONSECUTIVE_GAMES) {
                        // when there are 2 games directly before the proposed time, this time is not available
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean findCourt(LocalDateTime startTime, int numberOfCourts, List<Game> games) {
        int court = 0;
        for (Game game : games) {
            if (startTime.equals(game.getTime())) {
                court += 1;
            }
        }

        if (court < numberOfCourts) {
            court += 1;
            // TODO this maybe should not be the place to do this
            this.setLocation("Court " + court);
            this.setTime(startTime);
            return true;
        } else {
            return false;
        }
    }

    public static Game getLast(List<? extends Game> games) {
        if (games.size() == 0) {
            return null;
        }
        Game last = games.get(0);
        for (Game game : games) {
            if (game.getTime().isAfter(last.getTime())) {
                last = game;
            }
        }
        return last;
    }

    @Override
    public int compareTo(Game game) {
        if (this.time.equals(game.time)) {
            return this.location.compareTo(game.location);
        }
        return this.time.compareTo(game.time);
    }

    public void processResult() {
        // TODO I'm not sure why it was designed like this, maybe it can be easier?
        gameResult();
        result(getGameResult());
    }

    public void gameResult() {
        if (setPointsHomeSetOne > setPointsVisitorSetOne && setPointsHomeSetTwo > setPointsVisitorSetTwo
                && setPointsHomeSetThree > setPointsVisitorSetThree) {
            gameResult = "3 - 0";
        } else if (setPointsVisitorSetOne > setPointsHomeSetOne && setPointsVisitorSetTwo > setPointsHomeSetTwo
                && setPointsVisitorSetThree > setPointsHomeSetThree) {
            gameResult = "0 - 3";
        } else if (setPointsHomeSetOne > setPointsVisitorSetOne && setPointsVisitorSetTwo > setPointsHomeSetTwo
                && setPointsHomeSetThree > setPointsVisitorSetThree) {
            gameResult = "2 - 1";
        } else if (setPointsHomeSetOne > setPointsVisitorSetOne && setPointsVisitorSetTwo > setPointsHomeSetTwo
                && setPointsHomeSetThree < setPointsVisitorSetThree) {
            gameResult = "1 - 2";
        } else if (setPointsHomeSetOne < setPointsVisitorSetOne && setPointsVisitorSetTwo > setPointsHomeSetTwo
                && setPointsHomeSetThree > setPointsVisitorSetThree) {
            gameResult = "2 - 1";
        } else if (setPointsHomeSetOne > setPointsVisitorSetOne && setPointsVisitorSetTwo < setPointsHomeSetTwo
                && setPointsHomeSetThree < setPointsVisitorSetThree) {
            gameResult = "1 - 2";
        } else {
            gameResult = NOG_GEEN_UITSLAG_BEKEND;
        }
    }

    public void result(String getGameResult) {
        if (getGameResult == "3 - 0") {
            result = 1;
        } else if (getGameResult == "0 - 3") {
            result = 2;
        } else if (getGameResult == "2 - 1") {
            result = 1;
        } else if (getGameResult == "1 - 2") {
            result = 2;
        } else {
            result = 0;
        }
    }

    public Competitor getWinner() {
        switch (result) {
            case 1:
                return homeCompetitor;
            case 2:
                return visitorCompetitor;
            case 0:
            default:
                return null;
        }
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSetPointsHomeSetOne() {
        return setPointsHomeSetOne;
    }

    public void setSetPointsHomeSetOne(Integer setPointsHomeSetOne) {
        this.setPointsHomeSetOne = setPointsHomeSetOne;
    }

    public Integer getSetPointsVisitorSetOne() {
        return setPointsVisitorSetOne;
    }

    public void setSetPointsVisitorSetOne(Integer setPointsVisitorSetOne) {
        this.setPointsVisitorSetOne = setPointsVisitorSetOne;
    }

    public Integer getSetPointsHomeSetTwo() {
        return setPointsHomeSetTwo;
    }

    public void setSetPointsHomeSetTwo(Integer setPointsHomeSetTwo) {
        this.setPointsHomeSetTwo = setPointsHomeSetTwo;
    }

    public Integer getSetPointsVisitorSetTwo() {
        return setPointsVisitorSetTwo;
    }

    public void setSetPointsVisitorSetTwo(Integer setPointsVisitorSetTwo) {
        this.setPointsVisitorSetTwo = setPointsVisitorSetTwo;
    }

    public Integer getSetPointsHomeSetThree() {
        return setPointsHomeSetThree;
    }

    public void setSetPointsHomeSetThree(Integer setPointsHomeSetThree) {
        this.setPointsHomeSetThree = setPointsHomeSetThree;
    }

    public Integer getSetPointsVisitorSetThree() {
        return setPointsVisitorSetThree;
    }

    public void setSetPointsVisitorSetThree(Integer setPointsVisitorSetThree) {
        this.setPointsVisitorSetThree = setPointsVisitorSetThree;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    public Competitor getHomeCompetitor() {
        return homeCompetitor;
    }

    public void setHomeCompetitor(Competitor homeCompetitor) {
        this.homeCompetitor = homeCompetitor;
    }

    public Competitor getVisitorCompetitor() {
        return visitorCompetitor;
    }

    public void setVisitorCompetitor(Competitor visitorCompetitor) {
        this.visitorCompetitor = visitorCompetitor;
    }
}
