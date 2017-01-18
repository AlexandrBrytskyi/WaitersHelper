package brytskyi.waitershelperclient.app.activities.dishes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IDishService;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.UserType;
import transferFiles.service.restService.IAdminService;

import java.util.Arrays;
import java.util.LinkedList;


public class ConcretteDishFragment extends Fragment {

    private IDishService service = (IDishService) MyApplication.getService();
    private Dish selectedDish;
    private EditText name;
    private EditText price;
    private EditText description;
    private Spinner dishType;
    private Spinner whoCoock;
    private CheckBox availableCB;
    private boolean isChanged = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedDish = (Dish) MyApplication.getFromContext("selectedDish");
        return inflater.inflate(R.layout.fragment_concrette_dish, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = (EditText) getActivity().findViewById(R.id.dishNameEdit);
        price = (EditText) getActivity().findViewById(R.id.dishPriceEdit);
        description = (EditText) getActivity().findViewById(R.id.dishDescriptionEdit);
        dishType = (Spinner) getActivity().findViewById(R.id.dishTypeSpinner);
        whoCoock = (Spinner) getActivity().findViewById(R.id.whoCoockDishSpinner);
        availableCB = (CheckBox) getActivity().findViewById(R.id.availableCheckBox);

        dishType.setAdapter(new ArrayAdapter<DishType>(getActivity(), android.R.layout.simple_list_item_1, DishType.values()));
        whoCoock.setAdapter(new ArrayAdapter<UserType>(getActivity(), android.R.layout.simple_list_item_1, UserType.valuesWhoCoock()));
        setValues(selectedDish);
        setClicable(false);

        final Button changeButton = (Button) getActivity().findViewById(R.id.changeDishButton);
        changeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isChanged) {
                    changeButton.setText("Apply");
                    setClicable(true);
                    isChanged = true;
                } else {
                    selectedDish.setName(name.getText().toString());
                    selectedDish.setPriceForPortion(Double.valueOf(price.getText().toString()));
                    selectedDish.setType((DishType) dishType.getSelectedItem());
                    selectedDish.setWhoCoockDishType((UserType) whoCoock.getSelectedItem());
                    selectedDish.setDescription(description.getText().toString());
                    selectedDish.setIngridients(new LinkedList<Ingridient>());
                    selectedDish.setAvailable(availableCB.isChecked());
                    try {
                        selectedDish = service.updateDish(selectedDish);
                        showToast("Updated", getActivity());
                    } catch (Exception e) {
                        showToast(e.getMessage(), getActivity());
                    }
                    setClicable(false);
                    changeButton.setText(getString(R.string.change_dish));
                    isChanged = false;
                }
            }
        });
    }

    private void setClicable(boolean b) {
        name.setEnabled(b);
        price.setEnabled(b);
        description.setEnabled(b);
        dishType.setEnabled(b);
        whoCoock.setEnabled(b);
        availableCB.setEnabled(b);
    }

    private void setValues(Dish selectedDish) {
        name.setText(selectedDish.getName());
        price.setText(String.valueOf(selectedDish.getPriceForPortion()));
        description.setText(selectedDish.getDescription());
        dishType.setSelection(Arrays.asList(DishType.values()).indexOf(selectedDish.getType()));
        whoCoock.setSelection(Arrays.asList(UserType.valuesWhoCoock()).indexOf(selectedDish.getWhoCoockDishType()));
        availableCB.setChecked(selectedDish.isAvailable());
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

}
