package cl.cutiko.lotachilena.views.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.robohorse.gpversionchecker.GPVersionChecker;
import com.robohorse.gpversionchecker.base.CheckingStrategy;

import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.adapters.GamesListAdapter;
import cl.cutiko.lotachilena.models.game.Game;
import cl.cutiko.lotachilena.models.game.Queries;

public class GameListActivity extends AppCompatActivity {

    private RecyclerView gamesList;
    private GamesListAdapter gamesListAdapter;
    private List<Game> games;
    private BroadcastReceiver broadcastReceiver;
    private Queries gamesQueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkUpdates();

        gamesQueries = new Queries();
        games = gamesQueries.all();

        setBroadcastRecevier();

        setGamesList();

        setCreateGameBtn();
    }

    private void checkUpdates(){
        new GPVersionChecker.Builder(this)
                .setCheckingStrategy(CheckingStrategy.ONE_PER_DAY)
                .create();
    }

    private void setBroadcastRecevier() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("clickedGame")) {
                    clickedGamed(intent.getLongExtra("gameId", 0));
                } else if (intent.getAction().equals("longClickedGame")) {
                    longClickedGame(intent.getLongExtra("gameId", 0));
                }
            }
        };
    }

    private void clickedGamed(long gameId) {
        Intent goActivity = new Intent(this, BingoCallerActivity.class);
        goActivity.putExtra("gameId", gameId);
        startActivity(goActivity);
    }

    private void longClickedGame(final long gameId) {
        final Game game = gamesQueries.byId(gameId);
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.edit_title))
                .setMessage(getString(R.string.edit_msg) + " " + game.getName())
                .setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goActivity = new Intent(GameListActivity.this, CreateGameActivity.class);
                        goActivity.putExtra("gameId", gameId);
                        startActivity(goActivity);
                    }
                })
                .setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        game.delete();
                        games.clear();
                        List<Game> refreshList = gamesQueries.all();
                        if (refreshList != null && refreshList.size() > 0) {
                            for (Game gameToAdd : refreshList) {
                                games.add(gameToAdd);
                            }
                        }
                        gamesListAdapter.notifyDataSetChanged();

                        new cl.cutiko.lotachilena.models.chips.Queries().deleteByGame(gameId);

                        dialog.cancel();
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(R.mipmap.ic_mode_edit_black_24dp)
                .show();
    }

    private void setGamesList() {
        gamesList = (RecyclerView) findViewById(R.id.gamesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        gamesList.setLayoutManager(layoutManager);
        gamesList.setNestedScrollingEnabled(false);

        gamesListAdapter = new GamesListAdapter(games, this);
        gamesList.setAdapter(gamesListAdapter);
    }

    private void setCreateGameBtn() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goActivity = new Intent(getApplicationContext(), CreateGameActivity.class);
                startActivity(goActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        games.clear();
        List<Game> refreshGames = gamesQueries.all();
        if (refreshGames != null && refreshGames.size() > 0) {
            for (Game game : refreshGames) {
                games.add(game);
            }
        }
        gamesListAdapter.notifyDataSetChanged();

        IntentFilter filter = new IntentFilter();
        filter.addAction("clickedGame");
        filter.addAction("longClickedGame");
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {

        }
    }

}
