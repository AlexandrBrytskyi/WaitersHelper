package brytskyi.waitershelperclient.app.activities.users;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.exceptions.WrongFieldValueException;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IUserControl;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;
import transferFiles.password_utils.Password;
import transferFiles.service.restService.IAdminService;


public class AddUserActivity extends AppCompatActivity {

    private User logined = MyApplication.getLoginedUser();
    private IUserControl service = (IUserControl) MyApplication.getService();
    private EditText name;
    private EditText login;
    private CheckBox isLocked;
    private Spinner type;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initVars();
        setAddButtonListener();

    }



    private void setAddButtonListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUserDialog();
            }
        });
    }

    private void initVars() {
        name = (EditText) findViewById(R.id.userNameEditInAddUser);
        login = (EditText) findViewById(R.id.userLoginInAddUserToEdit);
        isLocked = (CheckBox) findViewById(R.id.lockedBoxInAddUser);
        type = (Spinner) findViewById(R.id.userTypeSpinnerInAddUser);
        addButton = (Button) findViewById(R.id.addUserButtonInUserActivity);

        ArrayAdapter<UserType> spinnerAdapter = new ArrayAdapter<UserType>(AddUserActivity.this, android.R.layout.simple_list_item_1, UserType.values());
        type.setAdapter(spinnerAdapter);
    }


    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void showAddUserDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Creating User").
                setMessage("Really create user?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            User created = createUserFromFields();
                            service.addNewUser(created);
                            showToast("Added " + created.getName(), AddUserActivity.this);
                        } catch (Exception e) {
                            showToast(e.getMessage(), AddUserActivity.this);
                        }
                        showToast("Success", AddUserActivity.this);
                    }
                }).
                setNegativeButton("NO", null).show();
    }

    private User createUserFromFields() throws WrongFieldValueException, WrongPasswordException {
        checkFields();
        return new User(
                name.getText().toString(),
                login.getText().toString(),
                new Password("00000").hashCode(),
                (UserType) type.getSelectedItem(),
                isLocked.isSelected()
        );
    }

    private void checkFields() throws WrongFieldValueException {
        if (name.getText().toString().equals("") || login.getText().toString().equals(""))
            throw new WrongFieldValueException("Name and login must be filled");
    }

}
