package edu.temple.webbrowser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return FragmentList.FRAGMENTS.get(i);
    }

    @Override
    public int getCount() {
        return FragmentList.FRAGMENTS.size();
    }
}
