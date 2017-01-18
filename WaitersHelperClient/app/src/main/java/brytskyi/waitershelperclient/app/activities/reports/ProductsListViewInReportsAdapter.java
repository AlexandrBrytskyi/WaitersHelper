package brytskyi.waitershelperclient.app.activities.reports;

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

public class ProductsListViewInReportsAdapter extends BaseAdapter {

    private List<Ingridient> ingridientList;
    private Activity activity;

    public ProductsListViewInReportsAdapter(Activity activity, List<Ingridient> ingridientsList) {
        super();
        this.activity = activity;
        this.ingridientList = ingridientsList;
    }

    @Override
    public int getCount() {
        return ingridientList.size();
    }

    @Override
    public Object getItem(int position) {
        return ingridientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView name;
        TextView mesurement;
        TextView amount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_list_row_in_reports, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.productNameInRowReports);
            holder.mesurement = (TextView) convertView.findViewById(R.id.productMesurementInReportsRow);
            holder.amount = (TextView) convertView.findViewById(R.id.productAmountInReportsRow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ingridient item = ingridientList.get(position);
        holder.name.setText(item.getProduct().getName());
        holder.mesurement.setText(item.getProduct().getMesuarment().toString());
        holder.amount.setText(String.valueOf(item.getAmount()));
        return convertView;
    }

    public void updateList(List<Ingridient> newList) {
        ingridientList = newList;
        notifyDataSetChanged();
    }
}