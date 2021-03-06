package cl.cutiko.lotachilena.views.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.adapters.PlayersAdapter;
import cl.cutiko.lotachilena.models.gamesPlayers.GamePlayer;
import cl.cutiko.lotachilena.models.players.Player;
import cl.cutiko.lotachilena.views.activities.photUtil.PhotoUtil;

public class PlayersFragment extends Fragment {

    private long gameId;

    View gameWinner, gameFlirt;

    private List<Player> players;
    private PlayersAdapter playersAdapter;
    private ListView gamePlayersList;

    private cl.cutiko.lotachilena.models.gamesPlayers.Queries gamePlayerQueries = new cl.cutiko.lotachilena.models.gamesPlayers.Queries();
    private cl.cutiko.lotachilena.models.players.Queries playerQueries = new cl.cutiko.lotachilena.models.players.Queries();

    public PlayersFragment() {
        // Required empty public constructor
    }

    public static PlayersFragment newInstance() {
        PlayersFragment fragment = new PlayersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_players, container, false);

        gameId = getActivity().getIntent().getExtras().getLong("gameId", 0);

        gameWinner = mainView.findViewById(R.id.gameWinner);
        long winnerId = gamePlayerQueries.gameWinner(gameId);
        setWinner(winnerId);

        gameFlirt = mainView.findViewById(R.id.gameFlirt);
        long flirtId = gamePlayerQueries.gameFlirt(gameId);
        setFlirt(flirtId);

        gamePlayersList = (ListView) mainView.findViewById(R.id.gamePlayersList);

        setCurrentPlayers();

        return mainView;
    }

    private void setWinner(final long winnerId) {
        if (winnerId == 0) {
            gameWinner.setVisibility(View.GONE);
        } else {
            Player player = playerQueries.byId(winnerId);

            gameWinner.setVisibility(View.VISIBLE);

            ImageView playerPhoto = (ImageView) gameWinner.findViewById(R.id.playerPhoto);
            TextView playerName = (TextView) gameWinner.findViewById(R.id.playerName);
            TextView winCount = (TextView) gameWinner.findViewById(R.id.winCount);
            TextView flirtCount = (TextView) gameWinner.findViewById(R.id.flirtCount);

            String photoName = player.getPhoto();
            if (photoName != null && !photoName.isEmpty()) {
                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + photoName);
                if (imgFile.exists()) {
                    Bitmap myBitmap = new PhotoUtil().getBitmapFromPath(imgFile.getAbsolutePath());
                    playerPhoto.setImageBitmap(myBitmap);
                } else {
                    playerPhoto.setImageResource(R.mipmap.ic_launcher);
                }
            } else {
                playerPhoto.setImageResource(R.mipmap.ic_launcher);
            }

            playerName.setText(player.getName());
            playerName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            winCount.setText(String.valueOf(player.getWinCount()));
            winCount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            winCount.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_action_star_10, 0, 0, 0);
            flirtCount.setText(String.valueOf(player.getFlirtCount()));

            gameWinner.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.delete_winner))
                            .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GamePlayer gamePlayer = gamePlayerQueries.intersection(gameId, winnerId);
                                    gamePlayer.setWinner(false);
                                    gamePlayer.save();
                                    gameWinner.setVisibility(View.GONE);
                                    playersAdapter.notifyDataSetChanged();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                    return false;
                }
            });
        }
    }

    private void setFlirt(final long flirtId) {
        if (flirtId == 0) {
            gameFlirt.setVisibility(View.GONE);
        } else {

            Player player = playerQueries.byId(flirtId);
            gameFlirt.setVisibility(View.VISIBLE);

            ImageView playerPhoto = (ImageView) gameFlirt.findViewById(R.id.playerPhoto);
            TextView playerName = (TextView) gameFlirt.findViewById(R.id.playerName);
            TextView winCount = (TextView) gameFlirt.findViewById(R.id.winCount);
            TextView flirtCount = (TextView) gameFlirt.findViewById(R.id.flirtCount);

            String photoName = player.getPhoto();
            if (photoName != null && !photoName.isEmpty()) {
                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + photoName);
                if (imgFile.exists()) {
                    Bitmap myBitmap = new PhotoUtil().getBitmapFromPath(imgFile.getAbsolutePath());
                    playerPhoto.setImageBitmap(myBitmap);
                } else {
                    playerPhoto.setImageResource(R.mipmap.ic_launcher);
                }
            } else {
                playerPhoto.setImageResource(R.mipmap.ic_launcher);
            }

            playerName.setText(player.getName());
            playerName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            winCount.setText(String.valueOf(player.getWinCount()));
            flirtCount.setText(String.valueOf(player.getFlirtCount()));
            flirtCount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            flirtCount.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_action_heart, 0, 0, 0);

            gameFlirt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.delete_winner))
                            .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GamePlayer gamePlayer = gamePlayerQueries.intersection(gameId, flirtId);
                                    gamePlayer.setFlirt(false);
                                    gamePlayer.save();
                                    gameFlirt.setVisibility(View.GONE);
                                    playersAdapter.notifyDataSetChanged();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                    return false;
                }
            });
        }
    }


    private void setCurrentPlayers() {

        List<GamePlayer> gamePlayerList = gamePlayerQueries.listedByGame(gameId);

        players = new ArrayList<>();

        if (gamePlayerList != null && gamePlayerList.size() > 0) {
            for (GamePlayer gamePlayer : gamePlayerList) {
                Player player = playerQueries.byId(gamePlayer.getPlayerId());
                players.add(player);
            }
        }

        playersAdapter = new PlayersAdapter(getContext(), R.layout.player_list_item, players);

        gamePlayersList.setAdapter(playersAdapter);

        gamePlayersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.winners_question))
                        .setMessage(getString(R.string.winner_instructions))
                        .setPositiveButton(getString(R.string.winner), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GamePlayer gamePlayer = gamePlayerQueries.intersection(gameId, id);
                                gamePlayer.setWinner(true);
                                gamePlayer.save();
                                playersAdapter.notifyDataSetChanged();
                                if (gameWinner.getVisibility() == View.VISIBLE) {
                                    Toast.makeText(getContext(), getString(R.string.repeated_winner), Toast.LENGTH_LONG).show();
                                } else {
                                    setWinner(id);
                                }
                                dialog.cancel();
                            }
                        })
                        .setNeutralButton(getString(R.string.flirt), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GamePlayer gamePlayer = gamePlayerQueries.intersection(gameId, id);
                                gamePlayer.setFlirt(true);
                                gamePlayer.save();
                                playersAdapter.notifyDataSetChanged();
                                if (gameFlirt.getVisibility() == View.VISIBLE) {
                                    Toast.makeText(getContext(), getString(R.string.repeated_winner), Toast.LENGTH_LONG).show();
                                } else {
                                    setFlirt(id);
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }
}
