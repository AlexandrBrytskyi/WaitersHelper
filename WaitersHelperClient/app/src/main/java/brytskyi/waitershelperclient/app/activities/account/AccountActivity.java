package brytskyi.waitershelperclient.app.activities.account;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.password_utils.Password;
import transferFiles.service.restService.IAccountable;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.ChangeNamePassRequest;

public class AccountActivity extends AppCompatActivity {

    private Button changeNameButton;
    private Button changePassButton;
    private EditText nameEdit;
    private EditText loginEdit;
    private User logined = MyApplication.getLoginedUser();
    private IAccountable service = (IAccountable) MyApplication.getService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initVars();
        initButtonListeners();

    }

    private void initButtonListeners() {
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNameDialog();
            }
        });

        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentPassDialog();
            }
        });
    }


    private void showChangeNameDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final EditText editText = new EditText(dialog.getContext());
        dialog.setTitle("Name").
                setMessage("Edit Name").
                setView(editText).
                setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logined = service.changeName(new ChangeNamePassRequest(logined, editText.getText().toString()));
                        updateFields();
                    }
                }).
                setNegativeButton("Cancel", null).show();
    }

    private void showCurrentPassDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final EditText editText = new EditText(dialog.getContext());
        dialog.setTitle("Password").
                setMessage("Enter current password").
                setView(editText).
                setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (new Password(editText.getText().toString()).hashCode() == logined.getPass()) {
                                showToast("Success", AccountActivity.this);
                                showChangePassDialog();
                            } else {
                                showToast("Invalid password", AccountActivity.this);
                            }
                        } catch (WrongPasswordException e) {
                            showToast("Invalid password: " + e.getMessage(), AccountActivity.this);
                        }
                    }
                }).
                setNegativeButton("Cancel", null).show();
    }

    private void showChangePassDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final EditText editText = new EditText(dialog.getContext());
        dialog.setTitle("Password").
                setMessage("Enter new password").
                setView(editText).
                setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            logined = service.changePassword(new ChangeNamePassRequest(logined, editText.getText().toString()));
                            showToast("Success", AccountActivity.this);
                            updateFields();
                        } catch (WrongPasswordException e) {
                            showToast(e.getMessage(), AccountActivity.this);
                        }
                    }
                }).
                setNegativeButton("Cancel", null).show();
    }


    private void initVars() {
        changeNameButton = (Button) findViewById(R.id.changeNamebutton);
        changePassButton = (Button) findViewById(R.id.changePassButton);
        nameEdit = (EditText) findViewById(R.id.userNameEditInAccount);
        loginEdit = (EditText) findViewById(R.id.userLoginEditInAccount);
    }


    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateFields();
        setTitle(logined.getType().toString());
    }

    private void updateFields() {
        nameEdit.setText(logined.getName());
        loginEdit.setText(logined.getLogin());
    }
}
