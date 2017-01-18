package brytskyi.waitershelperclient.app.activities.dishes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IDishService;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.RemoveDishRequest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class DishesListFragment extends Fragment {

    private IDishService service = (IDishService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();
    private DishesListViewAdapter adapter = null;
    private Spinner typeSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typeSpinner = (Spinner) getActivity().findViewById(R.id.dishTypeSpinnerInList);
        List<Object> types = new LinkedList<Object>(Arrays.asList(DishType.values()));
        types.add(0, "All");
        typeSpinner.setAdapter(new ArrayAdapter<Object>(getActivity(), android.R.layout.simple_list_item_1, types));

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).getClass().equals(String.class)) {
                    updateValues(adapter);
                } else {
                    updateValues(adapter, (DishType) parent.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setDishes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dishes_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) updateValues(adapter);
    }

    private void setDishes() {
        List<Dish> dishes = service.getAllDishes();
        adapter = new DishesListViewAdapter(getActivity(), dishes);
        ListView listView = (ListView) getActivity().findViewById(R.id.dishesList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("What now?")
                        .setMessage(getString(R.string.what_to_do))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Dish removed = service.removeDish(new RemoveDishRequest((Dish) parent.getItemAtPosition(position), logined));
                                    Toast.makeText(getActivity(), "Removed!!!\n" + removed.toString(), Toast.LENGTH_SHORT).show();
                                } catch (UserAccessException e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                updateValues(adapter);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConcretteDishActivity.class);
                intent.putExtra("dish", (Dish) parent.getItemAtPosition(position));
                startActivity(intent);
            }
        });

    }

    private void updateValues(DishesListViewAdapter adapter) {
        adapter.updateList(service.getAllDishes());
        adapter.notifyDataSetChanged();
    }

    private void updateValues(DishesListViewAdapter adapter, DishType type) {
        adapter.updateList(service.getDishesByDishType(type));
        adapter.notifyDataSetChanged();
    }


}