package cl.cutiko.lotachilena.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.models.chips.Chip;
import cl.cutiko.lotachilena.models.game.Game;
import cl.cutiko.lotachilena.models.game.Queries;

public class CallerFragment extends Fragment {

    private long gameId;
    private Game currentGame;
    private Queries gameQueries = new Queries();
    private int roundsCount;

    private cl.cutiko.lotachilena.models.chips.Queries chipsQueries = new cl.cutiko.lotachilena.models.chips.Queries();

    private TextView roundsCounter;
    private ImageButton callerBtn;
    private Button lastNumberBtn;

    private TextToSpeech textToSpeech;

    public CallerFragment() {
        // Required empty public constructor
    }

    public static CallerFragment newInstance() {
        CallerFragment fragment = new CallerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_caller, container, false);

        gameId = getActivity().getIntent().getExtras().getLong("gameId", 0);
        currentGame = gameQueries.byId(gameId);

        roundsCounter = (TextView) mainView.findViewById(R.id.roundsCounter);
        callerBtn = (ImageButton) mainView.findViewById(R.id.callerBtn);
        lastNumberBtn = (Button) mainView.findViewById(R.id.lastNumberBtn);

        setTextToSpeech();

        setCallerBtn();

        setLastNumberBtn();

        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        roundsCount = currentGame.getRounds();
        setRoundsCount();
        List<Chip> chips = chipsQueries.byGameId(gameId);
        if (chips != null && chips.size() > 0) {
            lastNumberBtn.setText(String.valueOf(chips.get(0).getPlayedNumber()));
        }
    }

    private void setRoundsCount() {
        roundsCounter.setText(roundsCount + " " + getContext().getString(R.string.current_rounds));
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

    private void setCallerBtn() {
        final Random random = new Random();
        callerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roundsCount == 90) {
                    readTheChip(getString(R.string.out_of_rounds));
                } else {
                    rollTheChips(random);
                }
            }
        });
    }

    private void rollTheChips(Random random) {
        int currentPlay = currentPlay(random);
        List<Chip> chips = chipsQueries.byGameId(gameId);

        if (chips != null && chips.size() > 0) {

            boolean notPlayed = true;

            for (Chip chip : chips) {
                if (chip.getPlayedNumber() == currentPlay) {
                    notPlayed = false;
                }
            }

            if (notPlayed) {
                randomSuccess(currentPlay);
            } else {
                rollTheChips(random);
            }

        } else {
            randomSuccess(currentPlay);
        }
    }

    private void randomSuccess(int chipNumber) {
        readTheChip(String.valueOf(chipNumber));
        roundsCount++;
        new Chip(chipNumber, roundsCount, gameId).save();
        currentGame.setRounds(roundsCount);
        currentGame.save();
        lastNumberBtn.setText(String.valueOf(chipNumber));
        setRoundsCount();
        Intent updateChipList = new Intent();
        updateChipList.setAction("updateChipList");
        getContext().sendBroadcast(updateChipList);
    }

    private int currentPlay(Random random) {
        return  random.nextInt(90 - 1 + 1) + 1;
    }

    private void readTheChip(String chip) {
        textToSpeech.speak(chip, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void setLastNumberBtn() {
        lastNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastNumber = lastNumberBtn.getText().toString();
                if (lastNumber != null && !lastNumber.isEmpty()) {
                    readTheChip(getString(R.string.last_number) + " " + lastNumber);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        textToSpeech.shutdown();
    }
}
