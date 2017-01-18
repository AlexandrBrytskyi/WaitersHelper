package brytskyi.waitershelperclient.app.activities.reports;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.denomination.Denomination;

import java.util.LinkedList;
import java.util.List;


public class DishesListInReportsFragment extends Fragment {


    private ListView dishesList;
    private DishesListViewInReportsAdapter dishAdapter;
    private TextView sumText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVars();

    }

    private void initVars() {
        dishesList = (ListView) getActivity().findViewById(R.id.dishesListInReports);
        dishAdapter = new DishesListViewInReportsAdapter(getActivity(), new LinkedList<Denomination>());
        dishesList.setAdapter(dishAdapter);
        sumText = (TextView) getActivity().findViewById(R.id.priceSumInReportsDishesList);
    }


    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dishes_list_in_reports, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateDishesList(List<Denomination> newList) {
        dishAdapter.updateList(newList);
        sumText.setText(String.valueOf(dishAdapter.getSum()));
    }

}