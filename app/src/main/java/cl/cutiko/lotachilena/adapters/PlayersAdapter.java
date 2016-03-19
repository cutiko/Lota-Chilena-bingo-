package cl.cutiko.lotachilena.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.models.chips.Chip;
import cl.cutiko.lotachilena.models.players.Player;
import cl.cutiko.lotachilena.views.activities.photUtil.PhotoUtil;

/**
 * Created by cutiko on 19-03-16.
 */
public class PlayersAdapter extends ArrayAdapter<Player> {

    public PlayersAdapter(Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder;
        if (convertView == null) {
            convertView  = layoutInflater.inflate(R.layout.player_list_item, parent, false);
            holder = new ViewHolder();
            holder.playerPhoto = (ImageView) convertView.findViewById(R.id.playerPhoto);
            holder.playerName = (TextView) convertView.findViewById(R.id.playerName);
            holder.winCount = (TextView) convertView.findViewById(R.id.winCount);
            holder.flirtCount = (TextView) convertView.findViewById(R.id.flirtCount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Player player = getItem(position);

        String photoName = player.getPhoto();
        if (photoName != null && !photoName.isEmpty()) {
            File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + photoName);
            if(imgFile.exists()){
                Bitmap myBitmap = new PhotoUtil().getBitmapFromPath(imgFile.getAbsolutePath());
                holder.playerPhoto.setImageBitmap(myBitmap);
            } else {
                holder.playerPhoto.setImageResource(R.mipmap.icon);
            }
        } else {
            holder.playerPhoto.setImageResource(R.mipmap.icon);
        }

        holder.playerName.setText(player.getName());

        holder.winCount.setText(String.valueOf(player.getWinCount()));
        holder.flirtCount.setText(String.valueOf(player.getFlirtCount()));


        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        ImageView playerPhoto;
        TextView playerName, winCount, flirtCount;
    }
}
