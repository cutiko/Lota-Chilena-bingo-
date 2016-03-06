package cl.cutiko.lotachilena.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.models.chips.Chip;

/**
 * Created by cutiko on 06-03-16.
 */
public class ChipsAdapter extends ArrayAdapter<Chip> {

    public ChipsAdapter(Context context, int resource, List<Chip> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolder holder;
        if (convertView == null) {
            convertView  = layoutInflater.inflate(R.layout.chip_list_item, parent, false);
            holder = new ViewHolder();
            holder.chipNumber = (TextView) convertView.findViewById(R.id.chipNumber);
            holder.chipRound = (TextView) convertView.findViewById(R.id.chipRound);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Chip chip = getItem(position);

        holder.chipNumber.setText(String.valueOf(chip.getPlayedNumber()));
        holder.chipRound.setText(String.valueOf(getContext().getString(R.string.chip_round) + " " + chip.getRound()));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        TextView chipNumber, chipRound;
    }

}
