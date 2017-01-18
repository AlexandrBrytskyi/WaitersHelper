package brytskyi.waitershelperclient.app.activities.products;

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
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IProductsService;
import transferFiles.exceptions.ProductByIdNotFoundException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;

import java.util.List;


public class ProductsListFragment extends Fragment {

    private IProductsService service = (IProductsService) MyApplication.getService();
    private User logined = MyApplication.getLoginedUser();
    private ProductsListViewAdapter adapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) updateValues();
    }



    private void setProducts() {
        List<Product> products = service.getAllProducts();
        adapter = new ProductsListViewAdapter(getActivity(), products);
        ListView listView = (ListView) getActivity().findViewById(R.id.productsList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Product removing")
                        .setMessage("Do you really want to remove this product?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    Product removed = service.removeProductById(((Product) parent.getItemAtPosition(position)).getId());
                                    Toast.makeText(getActivity(), "Removed!!!\n" + removed.toString(), Toast.LENGTH_SHORT).show();
                                    adapter.updateList(service.getAllProducts());
                                } catch (ProductByIdNotFoundException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "Sorry it`s already removed", Toast.LENGTH_SHORT).show();
                                } catch (UserAccessException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                updateValues();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }

        });
    }

    public void updateValues() {
        adapter.updateList(service.getAllProducts());
        adapter.notifyDataSetChanged();
    }


}