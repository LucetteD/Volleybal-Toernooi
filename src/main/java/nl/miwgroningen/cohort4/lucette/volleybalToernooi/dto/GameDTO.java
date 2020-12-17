package nl.miwgroningen.cohort4.lucette.volleybalToernooi.dto;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Game;

import javax.validation.constraints.Min;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
public class GameDTO {
    private Integer gameId;

    // TODO should there be an @Max? What is a reasonable Max value?
    @Min(0)
    private Integer setPointsHomeSetOne;
    @Min(0)
    private Integer setPointsVisitorSetOne;
    @Min(0)
    private Integer setPointsHomeSetTwo;
    @Min(0)
    private Integer setPointsVisitorSetTwo;
    @Min(0)
    private Integer setPointsHomeSetThree;
    @Min(0)
    private Integer setPointsVisitorSetThree;

    public GameDTO() {
    }

    public GameDTO(Game game) {
        gameId = game.getGameId();
        setPointsHomeSetOne = game.getSetPointsHomeSetOne();
        setPointsHomeSetTwo = game.getSetPointsHomeSetTwo();
        setPointsHomeSetThree = game.getSetPointsHomeSetThree();
        setPointsVisitorSetOne = game.getSetPointsVisitorSetOne();
        setPointsVisitorSetTwo = game.getSetPointsVisitorSetTwo();
        setPointsVisitorSetThree = game.getSetPointsVisitorSetThree();
    }

    public Game update(Game game) {
        if (this.gameId.equals(game.getGameId())) {
            game.setSetPointsHomeSetOne(setPointsHomeSetOne);
            game.setSetPointsHomeSetTwo(setPointsHomeSetTwo);
            game.setSetPointsHomeSetThree(setPointsHomeSetThree);
            game.setSetPointsVisitorSetOne(setPointsVisitorSetOne);
            game.setSetPointsVisitorSetTwo(setPointsVisitorSetTwo);
            game.setSetPointsVisitorSetThree(setPointsVisitorSetThree);
        }
        game.processResult();
        return game;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
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
}
