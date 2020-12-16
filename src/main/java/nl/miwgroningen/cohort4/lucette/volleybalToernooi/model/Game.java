package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.competitor.Competitor;
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gameId;

    private LocalDateTime time;

    private String location;

    // 0 = no winner/not yet played, 1 = home team won, 2 = visitor team won
    private int result = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "homeTeamId", referencedColumnName = "competitorId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competitor homeCompetitor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visitorTeamId", referencedColumnName = "competitorId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competitor visitorCompetitor;

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

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Competitor getHomeCompetitor() {
        return homeCompetitor;
    }

    public void setHomeCompetitor(Competitor homeTeam) {
        this.homeCompetitor = homeTeam;
    }

    public Competitor getVisitorCompetitor() {
        return visitorCompetitor;
    }

    public void setVisitorCompetitor(Competitor visitorTeam) {
        this.visitorCompetitor = visitorTeam;
    }

    public void findSlot(LocalDate gameDate, LocalTime startTime, LocalTime endTime,
                         int gameLength, int numberOfCourts, List<Game> games) {
        LocalTime gameTime = startTime;
        System.err.printf("Finding a slot for %s against %s\n", this.homeCompetitor, this.visitorCompetitor);
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
        for (Game game : games) {
            // if the competitor is in the game
            if (competitor.getTeam().equals(game.getHomeCompetitor().getTeam()) ||
                    competitor.getTeam().equals(game.getVisitorCompetitor().getTeam())) {
                if (game.time.isEqual(gameTime)) {
                    // and it is at the proposed time, that time is not available
                    return false;
                } else if (game.getTime().isAfter(gameTime.minusMinutes(MAX_CONSECUTIVE_GAMES * gameLength + 1))) {
                    // if it is within 2 game lengths before the proposed time, record it
                    // System.err.printf("%s is recorded because of a game at %s\n", gameTime, game.time);
                    precedingGames++;
                    if (precedingGames >= MAX_CONSECUTIVE_GAMES) {
                        // when there are 2 games directly before the proposed time, this time is not available
                        // System.err.println("Deny because too many games in a row for " + competitor);
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

    @Override
    public int compareTo(Game game) {
        if (this.time.equals(game.time)) {
            return this.location.compareTo(game.location);
        }
        return this.time.compareTo(game.time);
    }
}
