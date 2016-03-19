package cl.cutiko.lotachilena.models.gamesPlayers;

import com.orm.SugarRecord;

/**
 * Created by cutiko on 19-03-16.
 */
public class GamePlayer extends SugarRecord {

    private long gameId, playerId;
    private boolean winner, flirt;

    public GamePlayer() {
    }

    public GamePlayer(long gameId, long playerId, boolean winner, boolean flirt) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.winner = winner;
        this.flirt = flirt;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public boolean isFlirt() {
        return flirt;
    }

    public void setFlirt(boolean flirt) {
        this.flirt = flirt;
    }
}
