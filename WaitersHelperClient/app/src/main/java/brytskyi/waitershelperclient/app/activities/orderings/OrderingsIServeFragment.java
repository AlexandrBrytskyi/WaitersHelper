package brytskyi.waitershelperclient.app.activities.orderings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.activities.dishes.ConcretteDishActivity;
import brytskyi.waitershelperclient.app.activities.dishes.DishesListViewAdapter;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IOrderingService;
import org.joda.time.LocalDate;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.GetOrderingByDateBeginEndUserServesRequest;
import transferFiles.service.restService.restRequstObjects.RemoveDishRequest;
import transferFiles.service.restService.restRequstObjects.RemoveOrderingRequest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class OrderingsIServeFragment extends Fragment {

    private IOrderingService service = (IOrderingService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();
    private ListView ordersList;
    private OrderingsListViewAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ordersList = (ListView) getActivity().findViewById(R.id.orderingsIServeList);
        adapter = new OrderingsListViewAdapter(getActivity(), new LinkedList<Ordering>());
        ordersList.setAdapter(adapter);
        ordersList.setClickable(true);

        try {
            List<Ordering> iserve = service.getOrderingsUserServes(new GetOrderingByDateBeginEndUserServesRequest(logined, LocalDate.now(), LocalDate.now().plusDays(1)));
            if (iserve == null || iserve.isEmpty()) showToast("No results for today", getActivity());
            adapter.updateList(iserve);
        } catch (Exception e) {
            showToast(e.getMessage(), getActivity());
        }

        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("I am called! on item click");
                Intent intent = new Intent(getActivity(), ConcretteOrderingActivity.class);
                intent.putExtra("ordering", (Ordering) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        ordersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("What now?")
                        .setMessage(getString(R.string.what_to_do))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Ordering removed = service.removeOrdering(new RemoveOrderingRequest((Ordering) parent.getItemAtPosition(position), logined));
                                    adapter.removeFromList(removed);
                                    System.out.println(removed);
                                    Toast.makeText(getActivity(), "Removed!!!\n" + "Order number " + removed.getId(), Toast.LENGTH_SHORT).show();
                                } catch (UserAccessException e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orderings_i_serve_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}