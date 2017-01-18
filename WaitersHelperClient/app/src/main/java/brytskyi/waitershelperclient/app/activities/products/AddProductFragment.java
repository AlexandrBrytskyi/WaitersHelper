package brytskyi.waitershelperclient.app.activities.products;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IProductsService;
import transferFiles.model.dish.ingridient.Mesuarment;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.service.restService.IAdminService;


public class AddProductFragment extends Fragment {

    private IProductsService service = (IProductsService) MyApplication.getService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Spinner mesurementSpinner = (Spinner) getActivity().findViewById(R.id.mesurementSpinner);
        mesurementSpinner.setAdapter(new ArrayAdapter<Mesuarment>(getActivity(), android.R.layout.simple_list_item_1, Mesuarment.values()));

        Button createButton = (Button) getActivity().findViewById(R.id.createProductButton);
        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText name = (EditText) getActivity().findViewById(R.id.productNameEdit);
                if (name.getText() == null || name.getText().toString().equals("")) {
                    showToast("Please, enter name", getActivity());
                } else {
                    Product product = new Product();
                    product.setName(name.getText().toString());
                    product.setMesuarment((Mesuarment) mesurementSpinner.getSelectedItem());
                    Product added = service.addProduct(product);
                    showToast(added == null ? "Problem occured" : "Added " + added.toString(), getActivity());
                    name.setText("");
                }
            }
        });
    }

    private void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

}
