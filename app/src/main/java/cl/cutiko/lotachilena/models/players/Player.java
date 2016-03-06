package cl.cutiko.lotachilena.models.players;

import com.orm.SugarRecord;

/**
 * Created by cutiko on 06-03-16.
 */
public class Player extends SugarRecord {

    private String name;
    private long parentGame;

    public Player() {
    }

    public Player(String name, long parentGame) {
        this.name = name;
        this.parentGame = parentGame;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getParentGame() {
        return parentGame;
    }

    public void setParentGame(long parentGame) {
        this.parentGame = parentGame;
    }
}
