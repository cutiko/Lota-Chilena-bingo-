package cl.cutiko.lotachilena.views.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.models.gamesPlayers.GamePlayer;
import cl.cutiko.lotachilena.models.players.Player;
import cl.cutiko.lotachilena.views.activities.photUtil.PhotoUtil;

public class AddPlayersActivity extends AppCompatActivity {

    private Button addPlayerBtn, previousPlayerBtn, currentPlayersBtn;
    private Dialog addPlayerModal;
    private ImageView photoIv;

    private long gameId;
    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);

        gameId = getIntent().getLongExtra("gameId", 0);

        addPlayerBtn = (Button) findViewById(R.id.addPlayerBtn);
        previousPlayerBtn = (Button) findViewById(R.id.previousPlayerBtn);
        currentPlayersBtn = (Button) findViewById(R.id.currentPlayersBtn);

        setAddPlayerBtn();

    }

    private void setAddPlayerBtn(){
        addPlayerModal = new Dialog(this);
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
                    Player player = new Player(playerName, photo, 0);
                    player.save();
                    new GamePlayer(gameId, player.getId(), false, false).save();
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
