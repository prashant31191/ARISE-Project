package com.arise.ariseproject1.adapter;

import com.arise.ariseproject1.ProfileFragment;
import com.arise.ariseproject1.RadarFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
		// TODO Auto-generated constructor stub
	}

	@Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Profile fragment activity
            return new ProfileFragment();
        case 1:
            // Radar fragment activity
            return new RadarFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count = equal to number of tabs
        return 2;
    }
 
}
