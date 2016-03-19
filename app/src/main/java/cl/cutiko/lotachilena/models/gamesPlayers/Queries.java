package cl.cutiko.lotachilena.models.gamesPlayers;

import java.util.List;

/**
 * Created by cutiko on 19-03-16.
 */
public class Queries {

    public int playersByGame(long gameId) {
        List<GamePlayer> playersByGame = GamePlayer.find(GamePlayer.class, "game_id = ?", String.valueOf(gameId));
        if (playersByGame != null && playersByGame.size() > 0) {
            return playersByGame.size();
        } else {
            return 0;
        }
    }

}
