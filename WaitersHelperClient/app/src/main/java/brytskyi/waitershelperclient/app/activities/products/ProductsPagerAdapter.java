package brytskyi.waitershelperclient.app.activities.products;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProductsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
   private ProductsListFragment tab1;

    public ProductsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
               tab1 = new ProductsListFragment();
                return tab1;
            case 1:
                AddProductFragment tab2 = new AddProductFragment();
                return tab2;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void updateProducts() {
        tab1.updateValues();
    }
}