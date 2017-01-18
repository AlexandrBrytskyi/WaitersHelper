package brytskyi.waitershelperclient.app.activities.orderings;

import android.app.DatePickerDialog;
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
import brytskyi.waitershelperclient.app.exceptions.WrongFieldValueException;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IOrderingService;
import org.joda.time.LocalDate;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.dish.Dish;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.*;

import java.util.LinkedList;
import java.util.List;


public class OrderingsListFragment extends Fragment {

    private IOrderingService service = (IOrderingService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();

    private ListView orderList;
    private Spinner userTaken;
    private EditText dateBegin;
    private EditText dateEnd;
    private Button findButton;
    private LocalDate beginDate;
    private LocalDate endDate;
    private OrderingsListViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        setUserTakenAdapter();
        final LocalDate now = LocalDate.now();
        dateBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        beginDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        endDate = beginDate;
                        dateEnd.setText(endDate.toString());
                        dateBegin.setText(beginDate.toString());
                    }
                }, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth()).show();
            }
        });

        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        dateEnd.setText(endDate.toString());
                    }
                }, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth()).show();
            }
        });

        adapter = new OrderingsListViewAdapter(getActivity(), new LinkedList<Ordering>());
        orderList.setAdapter(adapter);

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConcretteOrderingActivity.class);
                intent.putExtra("ordering", (Ordering) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });


        orderList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    watchDate();
                    List<Ordering> updated = null;
                    if (beginDate.compareTo(endDate) == 0) {
                        if ((userTaken.getSelectedItemPosition() == 0)) {
                            updated = service.getOrderings(beginDate);
                            adapter.updateList(updated);
                            adapter.notifyDataSetChanged();
                        } else {
                            updated = service.getOrderings(new GetOrderingByDateUserTakenRequest((User) userTaken.getSelectedItem(), beginDate));
                            adapter.updateList(updated);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        if ((userTaken.getSelectedItemPosition() == 0)) {
                            updated = service.getOrderings(new GetByDateBeginEndRequest(beginDate, endDate));
                            adapter.updateList(updated);
                            adapter.notifyDataSetChanged();
                        } else {
                            updated = service.getOrderings(new GetOrderingByDateBeginEndUserTakenRequest((User) userTaken.getSelectedItem(), beginDate, endDate));
                            adapter.updateList(updated);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    if (updated == null || updated.isEmpty()) showToast("No orderings by such criteria", getActivity());
                } catch (WrongFieldValueException e) {
                    showToast(e.getMessage(), getActivity());
                }
            }
        });


    }

    private void setUserTakenAdapter() {
        List<User> users = service.getAllUsers();
        List<Object> filtered = new LinkedList<>();
        filtered.add("All");
        for (User user : users) {
            if (!user.getType().equals(UserType.COLD_KITCHEN_COCK) &&
                    !user.getType().equals(UserType.HOT_KITCHEN_COCK) &&
                    !user.getType().equals(UserType.MANGAL_COCK)) filtered.add(user);
        }
        ArrayAdapter<Object> userArrayAdapter = new ArrayAdapter<Object>(getActivity(), android.R.layout.simple_list_item_1, filtered);
        userTaken.setAdapter(userArrayAdapter);
    }

    private void watchDate() throws WrongFieldValueException {
        if (beginDate == null || endDate == null)
            throw new WrongFieldValueException("Date not setted");
        if (endDate.compareTo(beginDate) < 0)
            throw new WrongFieldValueException("Date end must be equal or more than date begin");
    }

    private void initVariables() {
        orderList = (ListView) getActivity().findViewById(R.id.allOrderingsList);
        userTaken = (Spinner) getActivity().findViewById(R.id.userTakenSpinnerInOrderinsList);
        dateBegin = (EditText) getActivity().findViewById(R.id.dateBeginPickerInOrderinsList);
        dateEnd = (EditText) getActivity().findViewById(R.id.dateEndPickerInOrderinsList);
        findButton = (Button) getActivity().findViewById(R.id.findOrderingsButtonInOrderingsList);
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orderings_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}