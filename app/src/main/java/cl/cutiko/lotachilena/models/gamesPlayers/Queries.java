package cl.cutiko.lotachilena.models.gamesPlayers;

import java.util.List;

import cl.cutiko.lotachilena.models.players.Player;

/**
 * Created by cutiko on 19-03-16.
 */
public class Queries {

    public int countByGame(long gameId) {
        List<GamePlayer> byGame = GamePlayer.find(GamePlayer.class, "game_id = ?", String.valueOf(gameId));
        if (byGame != null && byGame.size() > 0) {
            return byGame.size();
        } else {
            return 0;
        }
    }

    public List<GamePlayer> listedByGame(long gameId) {
        List<GamePlayer> byGame = GamePlayer.find(GamePlayer.class, "game_id = ?", String.valueOf(gameId));
        if (byGame != null && byGame.size() > 0) {
            return byGame;
        } else {
            return null;
        }
    }

    public int winsByPlayer(long playerId) {
        List<GamePlayer> winsByPlayer = GamePlayer.find(GamePlayer.class, "player_id = ? AND winner = 1", String.valueOf(playerId));
        if (winsByPlayer != null && winsByPlayer.size() > 0) {
            return winsByPlayer.size();
        } else {
            return 0;
        }
    }

    public int flirtByPlayer(long playerId) {
        List<GamePlayer> flirtsByPlayer = GamePlayer.find(GamePlayer.class, "player_id = ? AND flirt = 1", String.valueOf(playerId));
        if (flirtsByPlayer != null && flirtsByPlayer.size() > 0) {
            return flirtsByPlayer.size();
        } else {
            return 0;
        }
    }

    public long gameWinner(long gameId) {
        List<GamePlayer> byGame = listedByGame(gameId);
        if (byGame != null && byGame.size() > 0) {
            long winner = 0;
            for (GamePlayer gamePlayer : byGame) {
                if (gamePlayer.isWinner()) {
                    winner = gamePlayer.getPlayerId();
                }
            }
            return winner;
        } else {
            return 0;
        }
    }

    public long gameFlirt(long gameId) {
        List<GamePlayer> byGame = listedByGame(gameId);
        if (byGame != null && byGame.size() > 0) {
            long flirt = 0;
            for (GamePlayer gamePlayer : byGame) {
                if (gamePlayer.isFlirt()) {
                    flirt = gamePlayer.getPlayerId();
                }
            }
            return flirt;
        } else {
            return 0;
        }
    }

    public GamePlayer intersection(long gameId, long playerId) {
        return GamePlayer.find(GamePlayer.class, "game_id = ? AND player_id = ?", String.valueOf(gameId), String.valueOf(playerId)).get(0);
    }

}
