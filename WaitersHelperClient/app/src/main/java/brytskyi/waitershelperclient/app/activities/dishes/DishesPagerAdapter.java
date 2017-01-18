package brytskyi.waitershelperclient.app.activities.dishes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DishesPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
 
    public DishesPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
 
    @Override
    public Fragment getItem(int position) {
 
        switch (position) {
            case 0:
                DishesListFragment tab1 = new DishesListFragment();
                return tab1;
            case 1:
                AddDishFragment tab2 = new AddDishFragment();
                return tab2;
            default:
                return null;
        }
    }
 
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}