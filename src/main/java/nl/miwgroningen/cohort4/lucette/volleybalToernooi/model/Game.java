package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model;

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
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gameId;

    private LocalDateTime time;

    private String location;

    private int result;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "homeTeamId", referencedColumnName = "teamId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "visitorTeamId", referencedColumnName = "teamId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team visitorTeam;

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

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getVisitorTeam() {
        return visitorTeam;
    }

    public void setVisitorTeam(Team visitorTeam) {
        this.visitorTeam = visitorTeam;
    }

    public void findSlot(LocalDate startDate, LocalTime startTime, LocalTime endTime,
                         int gameLength, int numberOfCourts, List<Game> games) {
        LocalTime gameTime = startTime;
        while (!findCourt(LocalDateTime.of(startDate, gameTime), numberOfCourts, games)) {
            gameTime = gameTime.plusMinutes(gameLength);
            if (gameTime.isAfter(endTime)) {
                startDate = startDate.plusDays(1);
                gameTime = startTime;
            }
        }
    }

    private boolean findCourt(LocalDateTime startTime, int numberOfCourts, List<Game> games) {
        int court = 0;
        for (Game game : games) {
            if (startTime.equals(game.getTime())) {
                court += 1;

                if (    game.getHomeTeam().equals(this.getHomeTeam()) ||
                        game.getHomeTeam().equals(this.getVisitorTeam()) ||
                        game.getVisitorTeam().equals(this.getHomeTeam()) ||
                        game.getVisitorTeam().equals(this.getVisitorTeam())) {
                    return false;
                }
            }
        }

        if (court < numberOfCourts) {
            court += 1;
            this.setLocation("Court " + court);
            this.setTime(startTime);
            return true;
        } else {
            return false;
        }
    }
}
