package cl.cutiko.lotachilena.models.chips;

import com.orm.SugarRecord;

/**
 * Created by cutiko on 05-03-16.
 */
public class Chip extends SugarRecord {

    private int playedNumber, round;
    private long parentGame;

    public Chip() {
    }

    public Chip(int playedNumber, int round, long parentGame) {
        this.playedNumber = playedNumber;
        this.round = round;
        this.parentGame = parentGame;
    }

    public int getPlayedNumber() {
        return playedNumber;
    }

    public void setPlayedNumber(int playedNumber) {
        this.playedNumber = playedNumber;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public long getParentGame() {
        return parentGame;
    }

    public void setParentGame(long parentGame) {
        this.parentGame = parentGame;
    }
}
