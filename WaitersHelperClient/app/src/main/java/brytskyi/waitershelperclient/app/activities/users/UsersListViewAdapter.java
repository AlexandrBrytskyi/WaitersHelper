package brytskyi.waitershelperclient.app.activities.users;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.user.User;

import java.util.List;

public class UsersListViewAdapter extends BaseAdapter {

    private List<User> userList;
    private Activity activity;

    public UsersListViewAdapter(Activity activity, List<User> userList) {
        super();
        this.activity = activity;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView login;
        TextView name;
        TextView isLocked;
        TextView type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_list_row, null);
            holder = new ViewHolder();
            holder.login = (TextView) convertView.findViewById(R.id.userLoginInRowToEdit);
            holder.name = (TextView) convertView.findViewById(R.id.userNameInRowToEdit);
            holder.isLocked = (TextView) convertView.findViewById(R.id.userLockedInRowToEdit);
            holder.type = (TextView) convertView.findViewById(R.id.userTypeInRowToEdit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User item = userList.get(position);

        holder.login.setText(item.getLogin());
        holder.name.setText(item.getName());
        holder.isLocked.setSelected(item.isLocked());
        holder.isLocked.setText(item.isLocked() ? "Yes" : "No");
        holder.type.setText(item.getType().toString());
        return convertView;
    }

    public void removeFromList(User user) {
        userList.remove(user);
        this.notifyDataSetChanged();
    }

    public void updateList(List<User> newList) {
        userList = newList;
        this.notifyDataSetChanged();
    }
}