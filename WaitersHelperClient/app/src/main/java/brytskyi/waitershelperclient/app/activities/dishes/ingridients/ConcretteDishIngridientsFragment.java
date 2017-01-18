package brytskyi.waitershelperclient.app.activities.dishes.ingridients;

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
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IDishService;
import transferFiles.exceptions.IngridientWithIDNotFoundException;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.AddIngridientRequest;

import java.util.List;


public class ConcretteDishIngridientsFragment extends Fragment {

    private IDishService service = (IDishService) MyApplication.getService();
    private Dish selectedDish;
    private Spinner selectedIngrProductSpinner;
    private EditText selectedIngrAmountEdit;
    private Button addButton;
    private List<Product> products;
    private ArrayAdapter<Product> spinnerAdapter;
    private boolean changesMade = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedDish = (Dish) MyApplication.getFromContext("selectedDish");
        return inflater.inflate(R.layout.fragment_concrette_dish_ingridients, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        products = service.getAllProducts();
        spinnerAdapter = new ArrayAdapter<Product>(getActivity(), android.R.layout.simple_list_item_1, products);
        addButton = (Button) getActivity().findViewById(R.id.addIngridientButton);
        selectedIngrProductSpinner = (Spinner) getActivity().findViewById(R.id.productsSpinner);
        selectedIngrAmountEdit = (EditText) getActivity().findViewById(R.id.ingridientAmountEditInIngridFragment);
        setSelectedfieldsEnabled(false);

//        handle
        selectedIngrProductSpinner.setAdapter(spinnerAdapter);


        final ListView ingridientsList = (ListView) getActivity().findViewById(R.id.ingridientsList);
        // handle
        final IngrisientsListViewAdapter adapter = new IngrisientsListViewAdapter(getActivity(), service.getIngridientsByDish(selectedDish));
        ingridientsList.setAdapter(adapter);

        ingridientsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("What now?")
                        .setMessage(getString(R.string.what_to_do))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Ingridient removed = service.removeIngridientById(((Ingridient) parent.getItemAtPosition(position)).getId());
                                    Toast.makeText(getActivity(), "Removed!!!", Toast.LENGTH_SHORT).show();
                                } catch (IngridientWithIDNotFoundException e) {
                                    e.printStackTrace();
                                }
                                adapter.updateList(service.getIngridientsByDish(selectedDish));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changesMade) {
                    addButton.setText(getString(R.string.submit));
                    ingridientsList.setFocusable(false);
                    setSelectedfieldsEnabled(true);
                    selectedIngrAmountEdit.setText("");
                    changesMade = true;
                } else {
                    try {
                        Product prod = (Product) selectedIngrProductSpinner.getSelectedItem();
                        System.out.println(prod);
                        service.addIngridient(new AddIngridientRequest(prod,
                                Double.valueOf(selectedIngrAmountEdit.getText().toString()),
                                selectedDish));
                        adapter.updateList(service.getIngridientsByDish(selectedDish));
                        adapter.notifyDataSetChanged();
                        ingridientsList.setFocusable(true);
                        setSelectedfieldsEnabled(false);
                    } catch (NumberFormatException e) {
                        showToast("Wrong amount", getActivity());
                        changesMade = false;
                    }
                }
            }
        });


    }

    private void setSelectedfieldsEnabled(boolean b) {
        selectedIngrAmountEdit.setEnabled(b);
        selectedIngrProductSpinner.setEnabled(b);
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        products = service.getAllProducts();
        spinnerAdapter.notifyDataSetChanged();
    }
}
