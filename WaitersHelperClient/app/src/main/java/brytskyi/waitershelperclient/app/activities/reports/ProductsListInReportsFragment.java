package brytskyi.waitershelperclient.app.activities.reports;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;

import java.util.LinkedList;
import java.util.List;


public class ProductsListInReportsFragment extends Fragment {

    private ListView productsList;
    private ProductsListViewInReportsAdapter productsAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productsList = (ListView) getActivity().findViewById(R.id.productListInReports);
        productsAdapter = new ProductsListViewInReportsAdapter(getActivity(), new LinkedList<Ingridient>());
        productsList.setAdapter(productsAdapter);
    }


    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products_list_in_reports, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void updateProductsList(List<Ingridient> newList) {
        if (newList == null) return;
        if (newList.isEmpty()) showToast("No results", getActivity());
        productsAdapter.updateList(newList);
    }

}