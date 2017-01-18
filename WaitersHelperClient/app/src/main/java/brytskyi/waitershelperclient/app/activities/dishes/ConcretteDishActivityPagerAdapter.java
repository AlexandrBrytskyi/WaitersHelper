package brytskyi.waitershelperclient.app.activities.dishes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.activities.dishes.ingridients.ConcretteDishIngridientsFragment;
import transferFiles.model.dish.Dish;

public class ConcretteDishActivityPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;


    public ConcretteDishActivityPagerAdapter(FragmentManager fm, int NumOfTabs, Dish selectedDish) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        MyApplication.putInContext("selectedDish",selectedDish);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ConcretteDishFragment tab1 = new ConcretteDishFragment();
                return tab1;
            case 1:
                ConcretteDishIngridientsFragment tab2 = new ConcretteDishIngridientsFragment();
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