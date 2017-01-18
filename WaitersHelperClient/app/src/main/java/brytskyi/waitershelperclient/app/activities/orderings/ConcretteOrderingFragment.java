package brytskyi.waitershelperclient.app.activities.orderings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import transferFiles.exceptions.*;
import transferFiles.model.order.OrderType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.SetWhoServesOrderingRequest;

import java.io.IOException;


public class ConcretteOrderingFragment extends Fragment {

    private IOrderingService service = (IOrderingService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();
    private Ordering ordering = (Ordering) MyApplication.getFromContext("ordering");

    private TextView orderNum;
    private TextView timeAdded;
    private EditText datePicker;
    private EditText timePicker;
    private EditText amount;
    private TextView whoTaken;
    private TextView whoServes;
    private Spinner type;
    private EditText avance;
    private EditText description;
    private Button changeButton;
    private Button serveDropButton;
    private Button print;
    private ArrayAdapter<OrderType> typeAdapter;
    private boolean wasChanged = false;
    private LocalDate localDate;
    private LocalTime localTime;
    private EditText kotext;
    private AlertDialog.Builder printDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        initTypeSpinner();

        final LocalDateTime now = LocalDateTime.now();
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePicker.isFocusable())
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        localDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
                        datePicker.setText(localDate.toString());
                    }
                }, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth()).show();
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePicker.isFocusable())
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        localTime = new LocalTime(hourOfDay, minute);
                        timePicker.setText(localTime.toString());
                    }
                }, now.getHourOfDay(), now.getMinuteOfHour(), true).show();
            }
        });

        setFieldsEnabled(false);
        setValues();
        setButtons();
    }

    private void setValues() {
        orderNum.setText(String.valueOf(ordering.getId()));
        timeAdded.setText(ordering.getDateOrderCreated().toString());
        LocalDateTime clientsCome = ordering.getDateClientsCome();
        localDate = clientsCome.toLocalDate();
        localTime = clientsCome.toLocalTime();
        datePicker.setText(localDate.toString());
        timePicker.setText(localTime.toString());
        amount.setText(String.valueOf(ordering.getAmountOfPeople()));
        whoTaken.setText(ordering.getWhoTakenOrder().getName());
        whoServes.setText(ordering.getWhoServesOrder() != null ? ordering.getWhoServesOrder().getName() : "");
        type.setSelection(typeAdapter.getPosition(ordering.getType()));
        avance.setText(String.valueOf(ordering.getAdvancePayment()));
        description.setText(ordering.getDescription());
    }

    private void initTypeSpinner() {
        typeAdapter = new ArrayAdapter<OrderType>(getActivity(), android.R.layout.simple_list_item_1, OrderType.values());
        type.setAdapter(typeAdapter);
    }

    private void setButtons() {
        if (ordering.getWhoServesOrder() != null && ordering.getWhoServesOrder().equals(logined)) {
            serveDropButton.setText(getString(R.string.drop));
        }

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wasChanged) {
                    setFieldsEnabled(true);
                    changeButton.setText(getString(R.string.submit));
                    wasChanged = true;
                } else {
                    try {
                        watchFields();
                        ordering.setDateClientsCome(new LocalDateTime(
                                localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth(),
                                localTime.getHourOfDay(), localTime.getMinuteOfHour()
                        ));
                        ordering.setType((OrderType) type.getSelectedItem());
                        ordering.setAdvancePayment(Double.valueOf(avance.getText().toString()));
                        ordering.setAmountOfPeople(Integer.valueOf(amount.getText().toString()));
                        ordering.setDescription(description.getText().toString());
                        service.updateOrdering(ordering);
                        showToast("Updated", getContext());
                        wasChanged = false;
                        setFieldsEnabled(false);
                        changeButton.setText(getString(R.string.change));
                    } catch (WrongFieldValueException e) {
                        showToast(e.getMessage(), getActivity());
                    }
                }
            }
        });

        serveDropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serveDropButton.getText().toString().equals(getString(R.string.serve))) {
                    try {
                        service.setWhoServesOrder(new SetWhoServesOrderingRequest(ordering, logined));
                        showToast("Go and serve now!", getActivity());
                        serveDropButton.setText(getString(R.string.drop));
                    } catch (OrderingAlreadyServingException e) {
                        showToast(e.getMessage(), getActivity());
                    } catch (NoOrderingWithIdException e) {
                        showToast(e.getMessage(), getActivity());
                    } catch (UserAccessException e) {
                        showToast(e.getMessage(), getActivity());
                    }
                } else {
                    try {
                        service.setWhoServesOrderNull(new SetWhoServesOrderingRequest(ordering, logined));
                        showToast("Ok, you don`t serve this order now", getActivity());
                        serveDropButton.setText(getString(R.string.serve));
                    } catch (OrderingNotServingByYouException e) {
                        showToast(e.getMessage(), getActivity());
                    } catch (NoOrderingWithIdException e) {
                        showToast(e.getMessage(), getActivity());
                    } catch (UserAccessException e) {
                        showToast(e.getMessage(), getActivity());
                    }
                }
            }
        });



        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDialog = initDialog();
                printDialog.show();
            }
        });
    }

    private AlertDialog.Builder initDialog() {
        kotext = new EditText(getActivity());
        return new AlertDialog.Builder(getActivity())
                .setTitle("Sure?")

                .setMessage("Have You setted KO?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(kotext)
                .setPositiveButton("Print", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            double ko = Double.valueOf(kotext.getText().toString());
                            service.generatePrintPdf(service.setKO(String.valueOf(ko), ordering));
                            showToast("It`s ok, fund is printing", getActivity());
                        } catch (IOException e) {
                            showToast("Sorry, have problem", getActivity());
                        } catch (PrintingException e) {
                            showToast(e.getMessage(), getActivity());
                        } catch (NumberFormatException e) {
                            showToast("Wrong KO!", getActivity());
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null);
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void watchFields() throws WrongFieldValueException {
        if (description.getText() == null || description.getText().toString().equals(""))
            throw new WrongFieldValueException("Description must not be null");
        try {
            Integer.valueOf(amount.getText().toString());
            Double.valueOf((avance.getText() == null || avance.getText().toString().equals("")) ? "0" : avance.getText().toString());
        } catch (NumberFormatException e) {
            throw new WrongFieldValueException("Wrong amount, ko or avance");
        }
        if (localDate == null || localTime == null) throw new WrongFieldValueException("Date fields must be filled");
        if (localDate.compareTo(LocalDate.now()) > 0)
            throw new WrongFieldValueException("Wrong date");
    }

    private void setFieldsEnabled(boolean b) {
        datePicker.setEnabled(b);
        timePicker.setEnabled(b);
        amount.setEnabled(b);
        type.setEnabled(b);
        avance.setEnabled(b);
        description.setEnabled(b);
    }

    private void initVariables() {
        orderNum = (TextView) getActivity().findViewById(R.id.orderingNumberInConcretteOrderingToEdit);
        timeAdded = (TextView) getActivity().findViewById(R.id.orderingCreatedInConcretteOrderingToEdit);
        datePicker = (EditText) getActivity().findViewById(R.id.datePickerInConcretteOrderingToEdit);
        timePicker = (EditText) getActivity().findViewById(R.id.timePickerInConcretteOrderingToEdit);
        amount = (EditText) getActivity().findViewById(R.id.orderingAmountInConcretteOrderingToEdit);
        whoTaken = (TextView) getActivity().findViewById(R.id.whoTakenInConcretteOrderingToEdit);
        whoServes = (TextView) getActivity().findViewById(R.id.whoServesInConcretteOrderingToEdit);
        type = (Spinner) getActivity().findViewById(R.id.orderTypeSpinnerInConcretteOrdering);
        avance = (EditText) getActivity().findViewById(R.id.orderAvanseInConcretteOrderingEdit);
        description = (EditText) getActivity().findViewById(R.id.orderingDescriptionEditInConcretteOrdering);
        changeButton = (Button) getActivity().findViewById(R.id.changeOrderingButtonInConcretteOrdering);
        serveDropButton = (Button) getActivity().findViewById(R.id.serveOrderingButtonInConcretteOrdering);
        print = (Button) getActivity().findViewById(R.id.printOrderingButtonInConcretteOrdering);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ordering, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}