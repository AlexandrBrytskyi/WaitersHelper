package brytskyi.waitershelperclient.app.activities.dishes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Product;

import java.util.List;

public class DishesListViewAdapter extends BaseAdapter {

    private List<Dish> dishList;
    private Activity activity;

    public DishesListViewAdapter(Activity activity, List<Dish> dishList) {
        super();
        this.activity = activity;
        this.dishList = dishList;
    }

    @Override
    public int getCount() {
        return dishList.size();
    }

    @Override
    public Object getItem(int position) {
        return dishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView id;
        TextView name;
        TextView whoCoocks;
        TextView price;
        TextView type;
        TextView isAvailable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dish_list_row, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.dishId);
            holder.name = (TextView) convertView.findViewById(R.id.dishName);
            holder.whoCoocks = (TextView) convertView.findViewById(R.id.whoCoockDish);
            holder.price = (TextView) convertView.findViewById(R.id.priceForPortion);
            holder.type = (TextView) convertView.findViewById(R.id.typeDish);
            holder.isAvailable = (TextView) convertView.findViewById(R.id.isDishAvailable);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Dish item = dishList.get(position);
        holder.id.setText(String.valueOf(item.getId()));
        holder.name.setText(item.getName());
        holder.whoCoocks.setText(item.getWhoCoockDishType().toString());
        holder.price.setText(String.valueOf(item.getPriceForPortion()));
        holder.type.setText(item.getType().toString());
        holder.isAvailable.setText(item.isAvailable() ? "yes" : "no");
        return convertView;
    }

    public void updateList(List<Dish> newList) {
        dishList = newList;
    }
}