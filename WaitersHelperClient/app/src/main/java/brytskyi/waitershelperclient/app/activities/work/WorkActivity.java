package brytskyi.waitershelperclient.app.activities.work;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.activities.dishes.ConcretteDishActivity;
import brytskyi.waitershelperclient.app.tasksGetter.TasksGetter;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.user.User;
import transferFiles.service.restService.ICookService;
import transferFiles.service.restService.restRequstObjects.CockCancelDenomRequest;

import java.util.LinkedList;
import java.util.List;

public class WorkActivity extends AppCompatActivity {

    private User logined = MyApplication.getLoginedUser();
    private ICookService service = (ICookService) MyApplication.getService();
    private ListView denomsListView;
    private WorkDenominationsListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        denomsListView = (ListView) findViewById(R.id.workingDenomsListView);
        adapter = new WorkDenominationsListViewAdapter(WorkActivity.this, new LinkedList<Denomination>());
        MyApplication.putInContext("WorkDenomsAdapter", adapter);
        denomsListView.setAdapter(adapter);
        getWorkingDenoms();

        denomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(WorkActivity.this)
                        .setTitle("What now?")
                        .setMessage(getString(R.string.what_to_do))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Ready", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DenomTimeWrapper wrapper = ((DenomTimeWrapper) parent.getItemAtPosition(position));
                                Denomination denomination = wrapper.getDenomination();
                                try {
                                    service.setDenomStateReady(denomination);
                                    adapter.removeFromList(wrapper);
                                } catch (UserAccessException e) {
                                    showToast(e.getMessage(), WorkActivity.this);
                                }
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DenomTimeWrapper wrapper = ((DenomTimeWrapper) parent.getItemAtPosition(position));
                                Denomination denomination = wrapper.getDenomination();
                                try {
                                    service.cancelDenomination(new CockCancelDenomRequest(denomination, logined));
                                    adapter.removeFromList(wrapper);
                                } catch (UserAccessException e) {
                                    showToast(e.getMessage(), WorkActivity.this);
                                }
                            }
                        })
                        .setNegativeButton("Back", null).show();
            }
        });

        denomsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WorkActivity.this, ConcretteDishActivity.class);
                Dish dish = ((DenomTimeWrapper) parent.getItemAtPosition(position)).getDenomination().getDish();
                intent.putExtra("dish", dish);
                startActivity(intent);
                return true;
            }
        });

        Thread thread = new Thread(new TasksGetter());
        thread.start();


    }

    private void getWorkingDenoms() {
        List<Denomination> denominations = service.getWorkingDenoms(logined);
        if (denominations == null) denominations = new LinkedList<Denomination>();
        for (Denomination denomination : denominations) {
            adapter.addItem(denomination);
        }
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }


}
