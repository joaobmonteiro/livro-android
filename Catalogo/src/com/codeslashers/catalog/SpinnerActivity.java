package com.codeslashers.catalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class SpinnerActivity extends ActionBarActivity 
			implements OnNavigationListener {

	private SpinnerAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adapter = ArrayAdapter.createFromResource(this,
				R.array.spinner_options, R.layout.dropdown_item);

		getSupportActionBar()
			.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar()
			.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemId) {

		Fragment fragment = new SimpleFragment(position);

		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.container, fragment)
			.commit();

		return true;
	}
}