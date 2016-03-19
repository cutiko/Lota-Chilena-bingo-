package cl.cutiko.lotachilena.models.players;

import java.util.List;

/**
 * Created by cutiko on 19-03-16.
 */
public class Queries {

    public List<Player> every() {
        List<Player> players = Player.listAll(Player.class);
        if (players != null && players.size() > 0) {
            return players;
        } else {
            return null;
        }
    }

}
