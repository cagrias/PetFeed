package tr.com.minder.petfeed.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by cagri on 22.03.2016.
 */
public class LoginPagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public LoginPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SignInTabFragment tab1 = new SignInTabFragment();
                return tab1;
            case 1:
                SignUpTabFragment tab2 = new SignUpTabFragment();
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
