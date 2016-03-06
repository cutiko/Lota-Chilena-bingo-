package cl.cutiko.lotachilena.models.chips;

import java.util.Collections;
import java.util.List;

/**
 * Created by cutiko on 05-03-16.
 */
public class Queries {

    public List<Chip> byGameId(long gameId) {
        List<Chip> chips = Chip.find(Chip.class, "parent_game = ?", String.valueOf(gameId));
        if (chips != null && chips.size() > 0) {
            Collections.reverse(chips);
            return chips;
        } else {
            return chips;
        }
    }

    public Chip byId(long id) {
        return Chip.findById(Chip.class, id);
    }

    public void deleteByGame(long gameId) {
        Chip.deleteAll(Chip.class, "parent_game = ?", String.valueOf(gameId));
    }

}
