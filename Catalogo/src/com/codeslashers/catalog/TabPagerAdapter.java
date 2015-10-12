package com.codeslashers.catalog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter{
	
	private static final int NUM_TABS = 3;

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return new SimpleFragment(position);
	}

	@Override
	public int getCount() {
		return NUM_TABS;
	}
}