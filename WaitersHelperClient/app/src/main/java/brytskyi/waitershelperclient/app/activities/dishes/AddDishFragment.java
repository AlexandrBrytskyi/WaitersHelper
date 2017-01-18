package brytskyi.waitershelperclient.app.activities.dishes;

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
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IDishService;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Mesuarment;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.user.UserType;
import transferFiles.service.restService.IAdminService;

import java.util.LinkedList;


public class AddDishFragment extends Fragment {

    private IDishService service = (IDishService) MyApplication.getService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_dish, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText name = (EditText) getActivity().findViewById(R.id.dishNameEdit);
        final EditText price = (EditText) getActivity().findViewById(R.id.dishPriceEdit);
        final EditText description = (EditText) getActivity().findViewById(R.id.dishDescriptionEdit);
        final Spinner dishType = (Spinner) getActivity().findViewById(R.id.dishTypeSpinner);
        final Spinner whoCoock = (Spinner) getActivity().findViewById(R.id.whoCoockDishSpinner);
        dishType.setAdapter(new ArrayAdapter<DishType>(getActivity(), android.R.layout.simple_list_item_1, DishType.values()));
        whoCoock.setAdapter(new ArrayAdapter<UserType>(getActivity(), android.R.layout.simple_list_item_1, UserType.valuesWhoCoock()));


        Button addDishButton = (Button) getActivity().findViewById(R.id.addDishButton);
        addDishButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (name.getText() == null || name.getText().toString().equals("") ||
                        description.getText() == null || description.getText().toString().equals("") ||
                        price.getText() == null || price.getText().toString().equals("")) {
                    showToast(getString(R.string.all_fields_filled), getActivity());
                } else {
                    Dish added = null;
                    try {
                        Dish dish = new Dish();
                        dish.setName(name.getText().toString());
                        dish.setPriceForPortion(Double.valueOf(price.getText().toString()));
                        dish.setType((DishType) dishType.getSelectedItem());
                        dish.setWhoCoockDishType((UserType) whoCoock.getSelectedItem());
                        dish.setDescription(description.getText().toString());
                        dish.setIngridients(new LinkedList<Ingridient>());
                        dish.setAvailable(true);
                        added = service.addDish(dish);
                        showToast(added == null ? "Problem occured" : "Added " + added.toString(), getActivity());
                        Thread.currentThread().sleep(3000);
                        if (added != null) {
                            Intent intent = new Intent(getActivity(), ConcretteDishActivity.class);
                            intent.putExtra("dish", added);
                            startActivity(intent);
                        }
                    } catch (NumberFormatException e) {
                        showToast(getString(R.string.wrong_price), getActivity());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

}
