package cl.cutiko.lotachilena.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.adapters.ChipsAdapter;
import cl.cutiko.lotachilena.models.chips.Chip;


public class NumbersFragment extends Fragment {

    private long gameId;

    private cl.cutiko.lotachilena.models.chips.Queries chipsQueries = new cl.cutiko.lotachilena.models.chips.Queries();

    private ListView chipsList;
    private List<Chip> chips;
    private ChipsAdapter chipsAdapter;

    private TextToSpeech textToSpeech;

    private BroadcastReceiver broadcastReceiver;

    public NumbersFragment() {
        // Required empty public constructor
    }

    public static NumbersFragment newInstance(String param1, String param2) {
        NumbersFragment fragment = new NumbersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_numbers, container, false);

        gameId = getActivity().getIntent().getExtras().getLong("gameId", 0);
        chips = new ArrayList<>();
        chipsData();

        chipsAdapter = new ChipsAdapter(getContext(), R.layout.chip_list_item, chips);
        chipsList = (ListView) mainView.findViewById(R.id.chipsList);
        chipsList.setAdapter(chipsAdapter);

        setTextToSpeech();

        setChipClick();

        setReceiver();

        return mainView;
    }

    private void setReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("updateChipList")) {
                    chipsData();
                    chipsAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void setTextToSpeech() {
        final Locale spanish = new Locale("es", "ES");
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(spanish);
            }
        });
    }

    private void setChipClick() {
        chipsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chip chip = chipsQueries.byId(id);
                String chipInfo = getString(R.string.round) + " " + chip.getRound() + " " + getString(R.string.number) + " " + chip.getPlayedNumber();
                textToSpeech.speak(chipInfo, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    private void chipsData() {
        chips.clear();
        List<Chip> refreshChips = chipsQueries.byGameId(gameId);
        if (refreshChips != null && refreshChips.size() > 0) {
            for (Chip chip : refreshChips) {
                chips.add(chip);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateChipList");
        getContext().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            getContext().unregisterReceiver(broadcastReceiver);
        }catch (Exception e){

        }
    }


}
