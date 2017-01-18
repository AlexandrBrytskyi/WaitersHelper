package brytskyi.waitershelperclient.app.activities.orderings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.activities.dishes.ConcretteDishFragment;
import brytskyi.waitershelperclient.app.activities.dishes.ingridients.ConcretteDishIngridientsFragment;
import transferFiles.model.dish.Dish;

public class ConcretteOrderingActivityPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;


    public ConcretteOrderingActivityPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ConcretteOrderingFragment tab1 = new ConcretteOrderingFragment();
                return tab1;
            case 1:
                OrderingDenominationsFragment tab2 = new OrderingDenominationsFragment();
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