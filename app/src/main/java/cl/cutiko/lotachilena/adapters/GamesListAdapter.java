package cl.cutiko.lotachilena.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.models.game.Game;

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
        holder.gameName.setText(games.get(position).getName());
        holder.gameDate.setText(games.get(position).getDate());

        if (games.get(0) == games.get(position)) {
            holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.gameDate.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.gameName.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.gameDate.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.gameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clickedGamed = new Intent();
                clickedGamed.setAction("clickedGame");
                clickedGamed.putExtra("gameId", games.get(position).getId());
                context.sendBroadcast(clickedGamed);
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
        TextView gameName, gameDate;

        ViewHolder(View view) {
            super(view);
            gameContainer = (LinearLayout) view.findViewById(R.id.gameContainer);
            gameName = (TextView) view.findViewById(R.id.gameName);
            gameDate = (TextView) view.findViewById(R.id.gameDate);
        }

    }

}
