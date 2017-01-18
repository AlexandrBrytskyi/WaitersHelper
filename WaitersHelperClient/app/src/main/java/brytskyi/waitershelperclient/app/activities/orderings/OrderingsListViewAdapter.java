package brytskyi.waitershelperclient.app.activities.orderings;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.dish.Dish;
import transferFiles.model.order.Ordering;

import java.util.List;

public class OrderingsListViewAdapter extends BaseAdapter {

    private List<Ordering> orderingList;
    private Activity activity;

    public OrderingsListViewAdapter(Activity activity, List<Ordering> orderingList) {
        super();
        this.activity = activity;
        this.orderingList = orderingList;
    }

    @Override
    public int getCount() {
        return orderingList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView id;
        TextView timeAdded;
        TextView timeClientsCome;
        TextView amount;
        TextView whoTaken;
        TextView whoServes;
        TextView type;
        TextView avance;
        TextView description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ordering_list_row, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.orderingNumberInRow);
            holder.timeAdded = (TextView) convertView.findViewById(R.id.orderingCreatedInRow);
            holder.timeClientsCome = (TextView) convertView.findViewById(R.id.orderingclientComeInRow);
            holder.amount = (TextView) convertView.findViewById(R.id.orderingAmountInRow);
            holder.whoTaken = (TextView) convertView.findViewById(R.id.whoTakenInRow);
            holder.whoServes = (TextView) convertView.findViewById(R.id.whoServesInRow);
            holder.type = (TextView) convertView.findViewById(R.id.orderTypeInRow);
            holder.avance = (TextView) convertView.findViewById(R.id.orderavanseInRow);
            holder.description = (TextView) convertView.findViewById(R.id.orderingDescriptionEditInRow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ordering item = orderingList.get(position);
        holder.id.setText(String.valueOf(item.getId()));
        holder.timeAdded.setText(item.getDateOrderCreated().toString());
        holder.timeClientsCome.setText(item.getDateClientsCome().toString());
        holder.amount.setText(String.valueOf(item.getAmountOfPeople()));
        holder.whoTaken.setText(item.getWhoTakenOrder().getName());
        holder.whoServes.setText(item.getWhoServesOrder() == null ? "" : item.getWhoServesOrder().getName());
        holder.type.setText(item.getType().toString());
        holder.avance.setText(String.valueOf(item.getAdvancePayment()));
        holder.description.setText(item.getDescription());
        return convertView;
    }

    public void removeFromList(Ordering ordering) {
        orderingList.remove(ordering);
        this.notifyDataSetChanged();
    }

    public void updateList(List<Ordering> newList) {
        orderingList = newList;
        this.notifyDataSetChanged();
    }
}