package cl.cutiko.lotachilena.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.adapters.PlayersAdapter;
import cl.cutiko.lotachilena.models.gamesPlayers.GamePlayer;
import cl.cutiko.lotachilena.models.players.Player;

public class PlayersFragment extends Fragment {

    private List<Player> players;
    private PlayersAdapter playersAdapter;
    private ListView gamePlayersList;

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

        gamePlayersList = (ListView) mainView.findViewById(R.id.gamePlayersList);

        setCurrentPlayers();

        return mainView;
    }

    private void setCurrentPlayers() {
        long gameId = getActivity().getIntent().getExtras().getLong("gameId", 0);

        List<GamePlayer> gamePlayerList = new cl.cutiko.lotachilena.models.gamesPlayers.Queries().listedByGame(gameId);

        players = new ArrayList<>();
        cl.cutiko.lotachilena.models.players.Queries playerQueries = new cl.cutiko.lotachilena.models.players.Queries();

        for (GamePlayer gamePlayer : gamePlayerList) {
            Player player = playerQueries.byId(gamePlayer.getPlayerId());
            players.add(player);
        }

        playersAdapter = new PlayersAdapter(getContext(), R.layout.player_list_item, players);

        gamePlayersList.setAdapter(playersAdapter);

        gamePlayersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
}
