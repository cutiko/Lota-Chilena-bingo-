package cl.cutiko.lotachilena.models.players;

import com.orm.SugarRecord;

/**
 * Created by cutiko on 19-03-16.
 */
public class Player extends SugarRecord {

    private String name, photo;
    private int victories;

    public Player() {
    }

    public Player(String name, String photo, int victories) {
        this.name = name;
        this.photo = photo;
        this.victories = victories;
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

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }
}
