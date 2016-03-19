package cl.cutiko.lotachilena.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.models.game.Game;
import cl.cutiko.lotachilena.models.game.Queries;
import cl.cutiko.lotachilena.views.activities.photUtil.PhotoUtil;

public class CreateGameActivity extends AppCompatActivity {

    private EditText createGameName;
    private Button createGamePhoto, createGameSave;
    private ImageView createGameIv;
    private String photo;
    private long gameId;
    private Queries gameQueries = new Queries();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        createGameName = (EditText) findViewById(R.id.nameEt);
        createGamePhoto = (Button) findViewById(R.id.photoBtn);
        createGameIv = (ImageView) findViewById(R.id.photoIv);
        createGameSave = (Button) findViewById(R.id.createGameSave);

        gameId = getIntent().getLongExtra("gameId", 0);

        if (gameId != 0) {
            getSupportActionBar().setTitle(getString(R.string.editing));
            createGameSave.setText(getString(R.string.save_edit));
            setGameDataToEdit();
        }

        setPhotoBtn();

        setSaveBtn();

    }

    private void setGameDataToEdit() {
        Game game = gameQueries.byId(gameId);
        createGameName.setText(game.getName());
        photo = game.getPhoto();
        if (photo != null && !photo.isEmpty()) {
            createGameIv.setVisibility(View.VISIBLE);
            setPhoto();
        }
    }

    private void setPhotoBtn() {
        createGamePhoto.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(CreateGameActivity.this, getString(R.string.photo_cancelled), Toast.LENGTH_SHORT).show();
        }
    }

    private void setPhoto() {
        File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" +photo);
        if(imgFile.exists()){
            Bitmap myBitmap = new PhotoUtil().getBitmapFromPath(imgFile.getAbsolutePath());
            createGameIv.setVisibility(View.VISIBLE);
            createGameIv.setImageBitmap(myBitmap);
        }
    }

    private void setSaveBtn() {
        createGameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameName = createGameName.getText().toString();

                if (gameName != null && !gameName.isEmpty()) {

                    if (gameId != 0) {
                        Game game = gameQueries.byId(gameId);
                        game.setName(gameName);
                        game.setPhoto(photo);
                        game.save();

                        Intent goActivity = new Intent(getApplicationContext(), GameListActivity.class);
                        startActivity(goActivity);
                    } else {
                        Calendar calendar = new GregorianCalendar();
                        String date = calendar.get(Calendar.DAY_OF_MONTH)+ "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
                        Game game = new Game(gameName, date, photo, 0);
                        game.save();

                        Intent goActivity = new Intent(getApplicationContext(), AddPlayersActivity.class);
                        goActivity.putExtra("gameId", game.getId());
                        startActivity(goActivity);
                    }

                } else {
                    createGameName.setError(
                            getString(R.string.create_game_error_name));
                }

            }
        });
    }
}
