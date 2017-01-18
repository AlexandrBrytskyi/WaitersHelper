package brytskyi.waitershelperclient.app.activities.orderings;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.denomination.Denomination;

import java.util.LinkedList;
import java.util.List;

public class DenominationsListViewAdapter extends BaseAdapter {

    private List<Denomination> denominations;
    private Activity activity;

    public DenominationsListViewAdapter(Activity activity, List<Denomination> denominations) {
        super();
        this.activity = activity;
        this.denominations = new LinkedList<>(denominations);
    }

    @Override
    public int getCount() {
        return denominations.size();
    }

    @Override
    public Object getItem(int position) {
        return denominations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView dish;
        TextView price;
        TextView priceSum;
        TextView timeAdded;
        TextView timeReady;
        TextView state;
        TextView portions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.denomination_list_row, null);
            holder = new ViewHolder();
            holder.dish = (TextView) convertView.findViewById(R.id.dishNameInDenomRowToEdit);
            holder.price = (TextView) convertView.findViewById(R.id.priceForPortionInDemonRowToEdit);
            holder.priceSum = (TextView) convertView.findViewById(R.id.priceSumInDenomRowToEdit);
            holder.timeAdded = (TextView) convertView.findViewById(R.id.timeDenomAddedInDenomRowToEdit);
            holder.timeReady = (TextView) convertView.findViewById(R.id.timeDenomReadyInDenomRowToEdit);
            holder.state = (TextView) convertView.findViewById(R.id.denomStateInDenomsRowToEdit);
            holder.portions = (TextView) convertView.findViewById(R.id.portionsInDenomRowToEdit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Denomination item = denominations.get(position);
        holder.dish.setText(item.getDish().getName());
        holder.price.setText(String.valueOf(item.getDish().getPriceForPortion()));
        holder.priceSum.setText(String.valueOf(item.getPrice()));
        holder.timeAdded.setText(item.getTimeWhenAdded().toString());
        holder.timeReady.setText(item.getTimeWhenIsReady() != null ? item.getTimeWhenIsReady().toString() : "");
        holder.state.setText(item.getState().toString());
        holder.portions.setText(String.valueOf(item.getPortion()));
        return convertView;
    }

    public void removeFromList(Denomination denomination) {
        denominations.remove(denomination);
        this.notifyDataSetChanged();
    }

    public void addItem(Denomination denomination) {
        denominations.add(denomination);
        this.notifyDataSetChanged();
    }

    public void updateList(List<Denomination> newList) {
        denominations = new LinkedList<>(newList);
        this.notifyDataSetChanged();
    }

    public void updateDenomination(Denomination denomination) {
        Denomination den = null;
        for (Denomination denomination1 : denominations) {
            if (denomination1.getId() == denomination.getId()) den = denomination1;
        }
        int index = denominations.indexOf(den);
        denominations.set(index, denomination);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DenominationsListViewAdapter.this.notifyDataSetChanged();
            }
        });

    }

}