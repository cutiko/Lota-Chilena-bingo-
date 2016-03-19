package cl.cutiko.lotachilena.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.models.game.Game;
import cl.cutiko.lotachilena.views.activities.photUtil.PhotoUtil;

/**
 * Created by cutiko on 05-03-16.
 */
public class GamesListAdapter extends RecyclerView.Adapter<GamesListAdapter.ViewHolder>{

    private List<Game> games;
    private Context context;

    public GamesListAdapter(List<Game> games, Context context) {
        this.games = games;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String photoName = games.get(position).getPhoto();

        if (photoName != null && !photoName.isEmpty()) {
            File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + photoName);
            if(imgFile.exists()){
                Bitmap myBitmap = new PhotoUtil().getBitmapFromPath(imgFile.getAbsolutePath());
                holder.gamePhoto.setVisibility(View.VISIBLE);
                holder.gamePhoto.setImageBitmap(myBitmap);
            } else {
                holder.gamePhoto.setVisibility(View.GONE);
            }
        } else {
            holder.gamePhoto.setVisibility(View.GONE);
        }

        holder.gameName.setText(games.get(position).getName());

        if (games.get(0) == games.get(position)) {
            holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.gameDate.setText(games.get(position).getDate());

        //TODO fix this with the actual count
        holder.gamePlayersCount.setText(String.valueOf(8));

        holder.gameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clickedGamed = new Intent();
                clickedGamed.setAction("clickedGame");
                clickedGamed.putExtra("gameId", games.get(position).getId());
                context.sendBroadcast(clickedGamed);
            }
        });

        holder.gameContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent clickedGamed = new Intent();
                clickedGamed.setAction("longClickedGame");
                clickedGamed.putExtra("gameId", games.get(position).getId());
                context.sendBroadcast(clickedGamed);
                return true;
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout gameContainer;
        ImageView gamePhoto;
        TextView gameName, gamePlayersCount, gameDate;

        ViewHolder(View view) {
            super(view);
            gameContainer = (LinearLayout) view.findViewById(R.id.gameContainer);
            gamePhoto = (ImageView) view.findViewById(R.id.gamePhoto);
            gameName = (TextView) view.findViewById(R.id.gameName);
            gamePlayersCount = (TextView) view.findViewById(R.id.gamePlayersCount);
            gameDate = (TextView) view.findViewById(R.id.gameDate);

        }

    }

}
