package cl.cutiko.lotachilena.models.players;

import java.util.List;

/**
 * Created by cutiko on 06-03-16.
 */
public class Queries {

    public List<Player> byGame(long gameId) {
        List<Player> players = Player.find(Player.class, "parent_game = ?", String.valueOf(gameId));
        if (players != null && players.size() > 0) {
            return players;
        } else {
            return null;
        }
    }
}
