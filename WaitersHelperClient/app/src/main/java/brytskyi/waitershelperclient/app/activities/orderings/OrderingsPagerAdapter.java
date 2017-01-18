package brytskyi.waitershelperclient.app.activities.orderings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class OrderingsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public OrderingsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                OrderingsIServeFragment tab1 = new OrderingsIServeFragment();
                return tab1;
            case 1:
                OrderingsListFragment tab2 = new OrderingsListFragment();
                return tab2;
            case 2:
                AddOrderingFragment tab3 = new AddOrderingFragment();
                return tab3;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}