package ghamati.com.mfc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;


/**
 * Created by khashayar on 7/3/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private SparseArray<Fragment> fragments = null;
    private SparseArray<String> fragmentsTitle = null;

    public ViewPagerAdapter(FragmentManager fm, SparseArray<Fragment> fragments, SparseArray<String> fragmentsTitle) {
        super(fm);

        this.fragments = fragments;
        this.fragmentsTitle = fragmentsTitle;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitle.get(position);
    }


}
