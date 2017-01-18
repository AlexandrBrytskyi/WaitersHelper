package brytskyi.waitershelperclient.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.MyApplication;
import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;

import java.util.zip.Inflater;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((Button) findViewById(R.id.loginButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login = (EditText) findViewById(R.id.loginEdit);
                EditText pass = (EditText) findViewById(R.id.passwordEdit);
                try {
                    User logined = MyApplication.getValidator().login(login.getText().toString(), pass.getText().toString());
                    if (logined != null) {
                        showToast(logined.toString(), getApplicationContext());
                        runUserActivity(logined);
                    }
                } catch (WrongLoginException e) {
                    showToast(e.getMessage(), getApplicationContext());
                } catch (WrongPasswordException e) {
                    showToast(e.getMessage(), getApplicationContext());
                } catch (AccountBlockedException e) {
                    showToast(e.getMessage(), getApplicationContext());
                }
            }
        });
    }

    private void runUserActivity(User logined) {
        UserType type = logined.getType();
        Intent intent = null;
        if (type.equals(UserType.COLD_KITCHEN_COCK)
                || type.equals(UserType.HOT_KITCHEN_COCK)
                || type.equals(UserType.MANGAL_COCK)
                || type.equals(UserType.BARMEN)) {
            intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("logined", logined);
            startActivity(intent);
        } else {
            intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("logined", logined);
            startActivity(intent);
        }
    }


    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.getLoginedUser() != null) {
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("logined", MyApplication.getLoginedUser());
            startActivity(intent);
        }
    }
}
