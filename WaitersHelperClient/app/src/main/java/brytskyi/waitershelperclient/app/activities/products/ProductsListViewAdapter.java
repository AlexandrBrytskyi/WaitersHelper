package brytskyi.waitershelperclient.app.activities.products;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.user.User;

import java.util.LinkedList;
import java.util.List;

public class ProductsListViewAdapter extends BaseAdapter {

    private List<Product> productList;
    private Activity activity;

    public ProductsListViewAdapter(Activity activity, List<Product> productList) {
        super();
        this.activity = activity;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView id;
        TextView name;
        TextView mesur;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_list_row, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.sNo);
            holder.name = (TextView) convertView.findViewById(R.id.product);
            holder.mesur = (TextView) convertView.findViewById(R.id.category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product item = productList.get(position);
        holder.id.setText(String.valueOf(item.getId()));
        holder.name.setText(item.getName().toString());
        holder.mesur.setText(item.getMesuarment().toString());
        return convertView;
    }

    public void updateList(List<Product> newList) {
        productList = newList;
    }
}