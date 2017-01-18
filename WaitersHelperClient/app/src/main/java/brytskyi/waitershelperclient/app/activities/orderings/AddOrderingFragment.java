package brytskyi.waitershelperclient.app.activities.orderings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.exceptions.WrongFieldValueException;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IOrderingService;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.order.OrderType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;

import java.util.LinkedList;


public class AddOrderingFragment extends Fragment {

    private IOrderingService service = (IOrderingService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();

    private EditText date;
    private EditText time;
    private Spinner type;
    private EditText amount;
    private TextView whoTaken;
    private EditText avance;
    private EditText description;
    private Button addButton;
    private LocalDate localDate;
    private LocalTime localTime;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        whoTaken.setText(logined.getName());
        setTypeAdapter();

        final LocalDateTime now = LocalDateTime.now();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        localDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        date.setText(localDate.toString());
                    }
                }, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth()).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        localTime = new LocalTime(hourOfDay, minute);
                        time.setText(localTime.toString());
                    }
                }, now.getHourOfDay(), now.getMinuteOfHour(), true).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    watchFields();
                    Ordering newOrdering = new Ordering();
                    newOrdering.setDescription(description.getText().toString());
                    newOrdering.setAdvancePayment(Double.valueOf(avance.getText().toString()));
                    newOrdering.setAmountOfPeople(Integer.valueOf(amount.getText().toString()));
                    newOrdering.setDateClientsCome(new LocalDateTime(
                            localDate.getYear(),localDate.getMonthOfYear(),localDate.getDayOfMonth(),
                            localTime.getHourOfDay(), localTime.getMinuteOfHour()
                    ));
                    newOrdering.setDateOrderCreated(LocalDateTime.now());
                    newOrdering.setDenominations(new LinkedList<Denomination>());
                    newOrdering.setType((OrderType) type.getSelectedItem());
                    newOrdering.setWhoTakenOrder(logined);
                    newOrdering.setWhoServesOrder(((OrderType) type.getSelectedItem()).equals(OrderType.CURRENT) ? logined : null);
                    Ordering added = service.addOrder(newOrdering);
                    Intent intent = new Intent(getActivity(), ConcretteOrderingActivity.class);
                    intent.putExtra("ordering", added);
                    startActivity(intent);
                } catch (WrongFieldValueException e) {
                    showToast(e.getMessage(), getActivity());
                }
            }
        });
    }

    private void watchFields() throws WrongFieldValueException {
       if (localDate==null||localTime==null) throw new WrongFieldValueException("Set date and time");
        LocalDate now = LocalDate.now();
        if (now.compareTo(localDate)>0)
            throw new WrongFieldValueException("Wrong day");
        if (amount.getText() == null || amount.getText().toString().equals("") ||
                description.getText() == null || description.getText().toString().equals(""))
            throw new WrongFieldValueException("Fields must not be empty!");
        try {
            Integer.valueOf(amount.getText().toString());
            Double.valueOf(avance.getText().toString());
        } catch (NumberFormatException e) {
            throw new WrongFieldValueException("Wrong amount or payment");
        }

    }


    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void setTypeAdapter() {
        type.setAdapter(new ArrayAdapter<OrderType>(getActivity(), android.R.layout.simple_list_item_1, OrderType.values()));
    }

    private void initVariables() {
        date = (EditText) getActivity().findViewById(R.id.datePickerCreateOrdering);
        time = (EditText) getActivity().findViewById(R.id.timePickerCreateOrdering);
        type = (Spinner) getActivity().findViewById(R.id.orderTypeSpinnerInCreateOrdering);
        amount = (EditText) getActivity().findViewById(R.id.orderingAmountInCreateOrderingToEdit);
        whoTaken = (TextView) getActivity().findViewById(R.id.whoTakenInCreateOrderingToEdit);
        avance = (EditText) getActivity().findViewById(R.id.orderavanseInCreateOrderingEdit);
        description = (EditText) getActivity().findViewById(R.id.orderingDescriptionEditInCreateOrdering);
        addButton = (Button) getActivity().findViewById(R.id.addNewOrderingButtonInCreateOrdering);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_ordering, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}