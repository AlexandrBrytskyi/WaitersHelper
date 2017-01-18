package brytskyi.waitershelperclient.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.activities.account.AccountActivity;
import brytskyi.waitershelperclient.app.activities.dishes.DishesActivity;
import brytskyi.waitershelperclient.app.activities.menuUtils.MenuGetter;
import brytskyi.waitershelperclient.app.activities.orderings.OrderingsActivity;
import brytskyi.waitershelperclient.app.activities.products.ProductsActivity;
import brytskyi.waitershelperclient.app.activities.reports.ReportsActivity;
import brytskyi.waitershelperclient.app.activities.users.UsersActivity;
import brytskyi.waitershelperclient.app.activities.work.WorkActivity;
import brytskyi.waitershelperclient.app.tasksGetter.TasksGetter;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;

public class MainMenuActivity extends AppCompatActivity {

    private User logined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent = getIntent();
        logined = (User) intent.getExtras().get("logined");
        setMainMenuOptionsAndListener(logined);
        if (!logined.getType().equals(UserType.WAITER) && !logined.getType().equals(UserType.ADMIN) && !((boolean) MyApplication.getFromContext("workActivity"))) {
            Intent workIntent = new Intent(this, WorkActivity.class);
            MyApplication.putInContext("workActivity", true);
            startActivity(workIntent);
        } else {
            Thread thread = new Thread(new TasksGetter());
            thread.start();
        }
    }

    private void setMainMenuOptionsAndListener(final User logined) {
        ListView mainMenuOptions = (ListView) findViewById(R.id.mainMenuOptions);
        final UserType userType = logined.getType();
        mainMenuOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MenuGetter.getPoints(userType)));
        mainMenuOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch ((String) parent.getItemAtPosition(position)) {
                    case "Work":
                        intent = new Intent(MainMenuActivity.this, WorkActivity.class);
                        startActivity(intent);
                        return;
                    case "Orders":
                        intent = new Intent(MainMenuActivity.this, OrderingsActivity.class);
                        startActivity(intent);
                        return;
                    case "Users":
                        intent = new Intent(MainMenuActivity.this, UsersActivity.class);
                        startActivity(intent);
                        return;
                    case "Dishes":
                        intent = new Intent(MainMenuActivity.this, DishesActivity.class);
                        startActivity(intent);
                        return;
                    case "Products":
                        intent = new Intent(MainMenuActivity.this, ProductsActivity.class);
                        intent.putExtra("logined", logined);
                        startActivity(intent);
                        return;
                    case "Reports":
                        intent = new Intent(MainMenuActivity.this, ReportsActivity.class);
                        startActivity(intent);
                        return;
                    case "Account":
                        intent = new Intent(MainMenuActivity.this, AccountActivity.class);
                        startActivity(intent);
                        return;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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


}
