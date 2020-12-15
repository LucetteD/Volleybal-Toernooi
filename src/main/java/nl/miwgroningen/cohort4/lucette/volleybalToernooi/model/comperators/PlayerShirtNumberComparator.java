package nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.comperators;

import nl.miwgroningen.cohort4.lucette.volleybalToernooi.model.Player;

import java.util.Comparator;

/**
 * @author Vincent Velthuizen <v.r.velthuizen@pl.hanze.nl>
 */
public class PlayerShirtNumberComparator implements Comparator<Player> {
    @Override
    public int compare(Player player, Player t1) {
        return player.getShirtNumber() - t1.getShirtNumber();
    }
}
