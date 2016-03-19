package cl.cutiko.lotachilena.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cl.cutiko.lotachilena.R;

public class AddPlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
    }

    @Override
    public void onBackPressed() {
    }
}
