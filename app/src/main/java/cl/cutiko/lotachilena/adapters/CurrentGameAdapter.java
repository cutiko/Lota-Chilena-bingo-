package cl.cutiko.lotachilena.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cl.cutiko.lotachilena.views.fragments.CallerFragment;
import cl.cutiko.lotachilena.views.fragments.NumbersFragment;

/**
 * Created by cutiko on 05-03-16.
 */
public class CurrentGameAdapter extends FragmentPagerAdapter {

    public CurrentGameAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new CallerFragment();
                break;
            case 1:
                fragment = new NumbersFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Lota";
            case 1:
                return "Números";
        }
        return null;
    }
}
