package brytskyi.waitershelperclient.app.activities.dishes.ingridients;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;

import java.util.List;

public class IngrisientsListViewAdapter extends BaseAdapter {

    private List<Ingridient> ingridients;
    private Activity activity;

    public IngrisientsListViewAdapter(Activity activity, List<Ingridient> ingridList) {
        super();
        this.activity = activity;
        this.ingridients = ingridList;
    }

    @Override
    public int getCount() {
        return ingridients.size();
    }

    @Override
    public Object getItem(int position) {
        return ingridients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView product;
        TextView amount;
        TextView mesurement;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ingridients_list_row, null);
            holder = new ViewHolder();
            holder.product = (TextView) convertView.findViewById(R.id.ingridientProduct);
            holder.amount = (TextView) convertView.findViewById(R.id.ingridientAmount);
            holder.mesurement = (TextView) convertView.findViewById(R.id.ingridientMesur);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ingridient item = ingridients.get(position);
        holder.product.setText(item.getProduct().getName());
        holder.amount.setText(String.valueOf(item.getAmount()));
        holder.mesurement.setText(item.getProduct().getMesuarment().toString());
        return convertView;
    }

    public void updateList(List<Ingridient> newList) {
        ingridients = newList;
    }
}