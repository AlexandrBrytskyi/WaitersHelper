package brytskyi.waitershelperclient.app.activities.reports;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import brytskyi.waitershelperclient.app.activities.orderings.AddOrderingFragment;
import brytskyi.waitershelperclient.app.activities.orderings.OrderingsIServeFragment;
import brytskyi.waitershelperclient.app.activities.orderings.OrderingsListFragment;

public class ReportsPagerAdapter extends FragmentStatePagerAdapter {
   private int mNumOfTabs;
    private DishesListInReportsFragment tab1 = new DishesListInReportsFragment();
    private ProductsListInReportsFragment tab2 = new ProductsListInReportsFragment();

    public ReportsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                return tab1;
            case 1:
                return tab2;
            default:
                return null;
        }
    }


    public DishesListInReportsFragment getDishesFragment() {
        return tab1;
    }

    public ProductsListInReportsFragment getProductsFragment() {
        return tab2;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}