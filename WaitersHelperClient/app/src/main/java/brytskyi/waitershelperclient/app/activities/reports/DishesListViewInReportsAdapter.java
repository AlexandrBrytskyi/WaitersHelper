package brytskyi.waitershelperclient.app.activities.reports;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.denomination.Denomination;

import java.util.List;

public class DishesListViewInReportsAdapter extends BaseAdapter {

    private List<Denomination> denominationList;
    private Activity activity;
    private double sum = 0;

    public DishesListViewInReportsAdapter(Activity activity, List<Denomination> denominationList) {
        super();
        this.activity = activity;
        this.denominationList = denominationList;
    }

    @Override
    public int getCount() {
        return denominationList.size();
    }

    @Override
    public Object getItem(int position) {
        return denominationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView name;
        TextView type;
        TextView portion;
        TextView priceForPortion;
        TextView priceForAll;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dish_list_row, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.dishNameInReportRow);
            holder.type = (TextView) convertView.findViewById(R.id.dishTypeInReportRow);
            holder.portion = (TextView) convertView.findViewById(R.id.dishPortionInReportRow);
            holder.priceForPortion = (TextView) convertView.findViewById(R.id.priceForPortionInReportRow);
            holder.priceForAll = (TextView) convertView.findViewById(R.id.dishPriceInReportRow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Denomination item = denominationList.get(position);
        holder.name.setText(item.getDish().getName());
        holder.type.setText(String.valueOf(item.getDish().getType()));
        holder.portion.setText(String.valueOf(item.getPortion()));
        holder.priceForPortion.setText(String.valueOf(item.getDish().getPriceForPortion()));
        holder.priceForAll.setText(String.valueOf(item.getPrice()));
        return convertView;
    }

    public void updateList(List<Denomination> newList) {
        if (newList == null) return;
        if (newList.isEmpty()) {
            sum = 0;
        } else
            for (Denomination denomination : newList) {
                sum += denomination.getPrice();
            }
        denominationList = newList;
        notifyDataSetChanged();
    }

    public double getSum() {
        return sum;
    }
}