package cl.cutiko.lotachilena.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cl.cutiko.lotachilena.R;
import cl.cutiko.lotachilena.views.fragments.CallerFragment;
import cl.cutiko.lotachilena.views.fragments.NumbersFragment;
import cl.cutiko.lotachilena.views.fragments.PlayersFragment;

/**
 * Created by cutiko on 05-03-16.
 */
public class CurrentGameAdapter extends FragmentPagerAdapter {

    private Context context;

    public CurrentGameAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
            case 2:
                fragment = new PlayersFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.game);
            case 1:
                return context.getString(R.string.numbers);
            case 2:
                return context.getString(R.string.players);
        }
        return null;
    }
}
