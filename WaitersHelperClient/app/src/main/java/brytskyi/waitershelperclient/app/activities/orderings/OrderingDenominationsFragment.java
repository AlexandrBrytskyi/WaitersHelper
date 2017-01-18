package brytskyi.waitershelperclient.app.activities.orderings;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.LocalDateTime;
import transferFiles.exceptions.DenominationWithIdNotFoundException;
import transferFiles.exceptions.NoOrderingWithIdException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.AddCancelDenominationRequest;
import transferFiles.service.restService.restRequstObjects.RemoveDenominationRequest;
import transferFiles.service.restService.restRequstObjects.RemoveOrderingRequest;

import java.util.LinkedList;


public class OrderingDenominationsFragment extends Fragment {

    private IOrderingService service = (IOrderingService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();
    private Ordering ordering = (Ordering) MyApplication.getFromContext("ordering");
    private ListView denomsList;
    private Spinner dishTypeSpinner;
    private Spinner dishSpinner;
    private EditText portions;
    private Button addDenomButton;
    private DenominationsListViewAdapter adapter;
    private ArrayAdapter<Dish> dishArrayAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        findDenoms();
        setDishTypeSpinnerItems();
        addDenomButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    watchFields();
                    Denomination denomination = new Denomination();
                    denomination.setDish((Dish) dishSpinner.getSelectedItem());
                    denomination.setOrder(ordering);
                    denomination.setPortion(Double.valueOf(portions.getText().toString()));
                    denomination.setPrice(Double.valueOf(portions.getText().toString()) * denomination.getDish().getPriceForPortion());
                    denomination.setState(DenominationState.JUST_ADDED);
                    denomination.setTimeWhenAdded(LocalDateTime.now());
                    System.out.println(MyApplication.getMapper().writeValueAsString(denomination));
                    Denomination added = service.addDenomination(new AddCancelDenominationRequest(denomination, logined));
                    adapter.addItem(added);
                } catch (WrongFieldValueException e) {
                    showToast(e.getMessage(), getActivity());
                } catch (UserAccessException e) {
                    showToast(e.getMessage(), getActivity());
                } catch (NoOrderingWithIdException e) {
                    showToast(e.getMessage(), getActivity());
                }

                catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

        denomsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Really remove?")
                        .setMessage(getString(R.string.what_to_do))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Denomination removed = service.removeDenomination(
                                            new RemoveDenominationRequest((Denomination) parent.getItemAtPosition(position), logined));
                                    adapter.removeFromList(removed);
                                    Toast.makeText(getActivity(), "Removed!!!\n" + "Denomination " + removed.getDish().getName(), Toast.LENGTH_SHORT).show();
                                } catch (UserAccessException e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Denomination toCancel = (Denomination) parent.getItemAtPosition(position);
                                    service.cancelDenomination(new AddCancelDenominationRequest(toCancel, logined));
                                    Toast.makeText(getActivity(), "Canceled!!!", Toast.LENGTH_SHORT).show();
                                    adapter.updateList(service.getDenominationsByOrder(ordering));
                                } catch (UserAccessException e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                } catch (DenominationWithIdNotFoundException e) {
                                    showToast(e.getMessage(), getActivity());
                                }
                            }
                        })
                        .setNegativeButton("Back", null).show();
                return true;
            }
        });
    }

    private void watchFields() throws WrongFieldValueException {
        try {
            Double.valueOf(portions.getText().toString());
        } catch (NumberFormatException e) {
            throw new WrongFieldValueException("Wrong portions!");
        }
    }

    private void setDishTypeSpinnerItems() {
        dishTypeSpinner.setAdapter(new ArrayAdapter<DishType>(getContext(), android.R.layout.simple_list_item_1, DishType.values()));
        dishArrayAdapter = new ArrayAdapter<Dish>(getContext(), android.R.layout.simple_list_item_1);
        dishSpinner.setAdapter(dishArrayAdapter);

        dishTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDishesValues((DishType) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDishesValues(DishType type) {
        try {
            dishArrayAdapter.clear();
            dishArrayAdapter.addAll(service.getDishesByDishType(type));
            dishArrayAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            showToast(e.getMessage(), getActivity());
        }
    }

    private void findDenoms() {
        try {
            adapter = new DenominationsListViewAdapter(getActivity(), new LinkedList<Denomination>());
            adapter.updateList(service.getDenominationsByOrder(ordering));
            MyApplication.putInContext("denominationsAdapter", adapter);
            denomsList.setAdapter(adapter);
        } catch (Exception e) {
            showToast(e.getMessage(), getActivity());
        }
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void initVariables() {
        denomsList = (ListView) getActivity().findViewById(R.id.denominationsList);
        dishTypeSpinner = (Spinner) getActivity().findViewById(R.id.dishTypeSpinnerInDenominationsListFragment);
        dishSpinner = (Spinner) getActivity().findViewById(R.id.dishSpinnerInDenominationsListFragment);
        portions = (EditText) getActivity().findViewById(R.id.portionsAmountInDenominationListFragment);
        addDenomButton = (Button) getActivity().findViewById(R.id.addDenominationButtonInDenominationListFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_denominations_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}