package brytskyi.waitershelperclient.app.activities.users;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IUserControl;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.LockUserRequest;


public class UsersActivity extends AppCompatActivity {

    private User logined = MyApplication.getLoginedUser();
    private IUserControl service = (IUserControl) MyApplication.getService();
    private ListView usersList;
    private Button addUserButton;
    private UsersListViewAdapter usersAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initVars();
        setListAdapter();
        setListListener();
        setAddButtonListener();
    }

    private void setAddButtonListener() {
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsersActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setListListener() {

        usersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showLockUserDialog((User) parent.getItemAtPosition(position));
                return true;
            }
        });
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showLockUserDialog(final User user) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Bahn").
                setMessage(user.isLocked() ? "Unlock " : "Lock " + "user " + user.getName() + "?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        service.lockUser(new LockUserRequest(user, user.isLocked() ? true : false));
                        showToast("Success", UsersActivity.this);
                        user.setLocked(user.isLocked() ? false : true);
                        usersAdapter.notifyDataSetChanged();
                    }
                }).
                setNegativeButton("NO", null).show();
    }

    private void setListAdapter() {
        usersList.setAdapter(usersAdapter);
    }

    private void initVars() {
        usersList = (ListView) findViewById(R.id.usersListViewInUsersActivity);
        addUserButton = (Button) findViewById(R.id.addUserButtonInUsersActivity);
        usersAdapter = new UsersListViewAdapter(this, service.getAllUsers());
    }


    @Override
    protected void onResume() {
        super.onResume();
        usersAdapter.updateList(service.getAllUsers());
    }
}
