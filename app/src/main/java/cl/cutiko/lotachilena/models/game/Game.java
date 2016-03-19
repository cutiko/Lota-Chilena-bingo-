package cl.cutiko.lotachilena.models.game;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by cutiko on 05-03-16.
 */
public class Game extends SugarRecord{

    private String name, date, photo;
    private int rounds;
    @Ignore
    private int playersCount;

    public Game() {
    }

    public Game(String name, String date, String photo, int rounds) {
        this.name = name;
        this.date = date;
        this.photo = photo;
        this.rounds = rounds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getPlayersCount() {
        return new cl.cutiko.lotachilena.models.gamesPlayers.Queries().countByGame(getId());
    }
}
