package cl.cutiko.lotachilena.models.game;

import java.util.Collections;
import java.util.List;

/**
 * Created by cutiko on 05-03-16.
 */
public class Queries {

    public List<Game> all() {
        List<Game> games = Game.listAll(Game.class);
        Collections.reverse(games);
        return games;
    }

    public Game byId(long id) {
        return Game.findById(Game.class, id);
    }

}
