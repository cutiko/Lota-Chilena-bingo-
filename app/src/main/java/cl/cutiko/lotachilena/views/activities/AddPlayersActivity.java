package cl.cutiko.lotachilena.views.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.adapters.PlayersAdapter;
import cl.cutiko.lotachilena.models.gamesPlayers.GamePlayer;
import cl.cutiko.lotachilena.models.players.Player;
import cl.cutiko.lotachilena.models.players.Queries;
import cl.cutiko.lotachilena.views.activities.photUtil.PhotoUtil;

public class AddPlayersActivity extends AppCompatActivity {

    private Button addPlayerBtn, previousPlayerBtn, currentPlayersBtn;
    private ImageView photoIv;

    private long gameId;
    private String photo;

    private List<Player> everyPlayer;
    private PlayersAdapter previousPlayersAdapter;

    private List<Player> currentPlayers;
    private PlayersAdapter currentPlayersAdapter;

    private cl.cutiko.lotachilena.models.players.Queries playersQueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);

        gameId = getIntent().getLongExtra("gameId", 0);
        playersQueries = new Queries();

        addPlayerBtn = (Button) findViewById(R.id.addPlayerBtn);
        previousPlayerBtn = (Button) findViewById(R.id.previousPlayerBtn);
        currentPlayersBtn = (Button) findViewById(R.id.currentPlayersBtn);

        setAddPlayerBtn();

        List<Player> players = playersQueries.every();

        if (players != null && players.size() > 0) {
            previousPlayerBtn.setText(getString(R.string.previous_player) + " (" + String.valueOf(players.size()) + ")");
            setPreviousPlayerBtn();
        } else {
            previousPlayerBtn.setText(getString(R.string.previous_player) + " (0)");
            previousPlayerBtn.setEnabled(false);
            previousPlayerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        }

        setCurrentPlayersBtn();

        setStartGame();

    }

    private void setAddPlayerBtn(){
        final Dialog addPlayerModal = new Dialog(this);
        addPlayerModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addPlayerModal.setCancelable(false);

        addPlayerModal.setContentView(R.layout.create_player_modal);
        final EditText nameEt = (EditText) addPlayerModal.findViewById(R.id.nameEt);
        Button photoBtn = (Button) addPlayerModal.findViewById(R.id.photoBtn);
        photoIv = (ImageView) addPlayerModal.findViewById(R.id.photoIv);

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File photo = null;
                try {
                    photo = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                startActivityForResult(intent, 111);
            }
        });

        Button playerModalCancel = (Button) addPlayerModal.findViewById(R.id.playerModalCancel);
        Button playerModalSave = (Button) addPlayerModal.findViewById(R.id.playerModalSave);

        playerModalCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayerModal.cancel();
            }
        });

        playerModalSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = nameEt.getText().toString();
                if (playerName != null && !playerName.isEmpty()) {
                    Player player = new Player(playerName, photo);
                    player.save();
                    new GamePlayer(gameId, player.getId(), false, false).save();
                    currentPlayers.add(player);
                    currentPlayersAdapter.notifyDataSetChanged();
                    addPlayerModal.cancel();
                } else {
                    nameEt.setError(getString(R.string.error_name));
                }
            }
        });

        addPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayerModal.show();
            }
        });
    }

    private void setPreviousPlayerBtn() {
        everyPlayer = playersQueries.every();
        previousPlayersAdapter = new PlayersAdapter(this, R.layout.player_list_item, everyPlayer);

        final Dialog previousPlayersModal = new Dialog(this);
        previousPlayersModal.requestWindowFeature(Window.FEATURE_NO_TITLE);

        previousPlayersModal.setContentView(R.layout.players_list_modal);
        ListView playersModalList = (ListView) previousPlayersModal.findViewById(R.id.playersModalList);
        playersModalList.setAdapter(previousPlayersAdapter);

        playersModalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPlayers.add(playersQueries.byId(id));
                currentPlayersAdapter.notifyDataSetChanged();
                new GamePlayer(gameId, id, false, false).save();
                everyPlayer.remove(position);
                previousPlayersAdapter.notifyDataSetChanged();
                previousPlayersModal.cancel();
            }
        });

        previousPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousPlayersModal.show();
            }
        });

    }

    private void setCurrentPlayersBtn() {
        currentPlayers = new ArrayList<>();
        currentPlayersAdapter = new PlayersAdapter(this, R.layout.player_list_item, currentPlayers);

        final Dialog currentPlayersModal = new Dialog(this);
        currentPlayersModal.requestWindowFeature(Window.FEATURE_NO_TITLE);

        currentPlayersModal.setContentView(R.layout.players_list_modal);
        ListView playersModalList = (ListView) currentPlayersModal.findViewById(R.id.playersModalList);
        playersModalList.setAdapter(currentPlayersAdapter);

        playersModalList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentPlayers.remove(position);
                currentPlayersAdapter.notifyDataSetChanged();
                currentPlayersModal.cancel();
                return false;
            }
        });

        currentPlayersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayers != null && currentPlayers.size() > 0) {
                    currentPlayersModal.show();
                } else {
                    Toast.makeText(AddPlayersActivity.this, getString(R.string.players_intructions), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setStartGame() {
        Button startGame = (Button) findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goActivity = new Intent(AddPlayersActivity.this, BingoCallerActivity.class);
                goActivity.putExtra("gameId", gameId);
                startActivity(goActivity);
                finish();
            }
        });
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "smartrace" + "_" + String.valueOf(System.currentTimeMillis()/1000);

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        );

        photo = image.getName();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            setPhoto();
        } else {
            photo = null;
            Toast.makeText(AddPlayersActivity.this, getString(R.string.photo_cancelled), Toast.LENGTH_SHORT).show();
        }
    }

    private void setPhoto() {
        File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" +photo);
        if(imgFile.exists()){
            Bitmap myBitmap = new PhotoUtil().getBitmapFromPath(imgFile.getAbsolutePath());
            photoIv.setVisibility(View.VISIBLE);
            photoIv.setImageBitmap(myBitmap);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
