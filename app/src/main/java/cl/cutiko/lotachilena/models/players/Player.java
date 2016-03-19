package cl.cutiko.lotachilena.models.players;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by cutiko on 19-03-16.
 */
public class Player extends SugarRecord {

    private String name, photo;

    @Ignore
    private int winCount, flirtCount;

    public Player() {
    }

    public Player(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getWinCount() {
        return new cl.cutiko.lotachilena.models.gamesPlayers.Queries().winsByPlayer(getId());
    }

    public int getFlirtCount() {
        return new cl.cutiko.lotachilena.models.gamesPlayers.Queries().flirtByPlayer(getId());
    }
}
