package cl.cutiko.lotachilena.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.adapters.GamesListAdapter;
import cl.cutiko.lotachilena.models.game.Game;
import cl.cutiko.lotachilena.models.game.Queries;
import cl.cutiko.lotachilena.views.activities.photUtil.PhotoUtil;

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

        gamesQueries = new Queries();
        games = gamesQueries.all();

        if (games != null && games.size() > 0) {
            setLastGamePhoto();
        }

        setBroadcastRecevier();

        setGamesList();

        setCreateGameBtn();
    }

    private void setLastGamePhoto() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        //TODO add a cool background photo
    }

    private void setBroadcastRecevier() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("clickedGame")) {
                    clickedGamed(intent.getLongExtra("gameId", 0));
                }
            }
        };
    }

    private void clickedGamed(long gameId) {
        Intent goActivity = new Intent(this, BingoCallerActivity.class);
        goActivity.putExtra("gameId", gameId);
        startActivity(goActivity);
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
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            unregisterReceiver(broadcastReceiver);
        }catch (Exception e){

        }
    }

}
