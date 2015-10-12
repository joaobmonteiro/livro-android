package com.codeslashers.catalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleFragment extends Fragment {
	
	private int position;
	
	public SimpleFragment(){}
	
	public SimpleFragment(int position) {
		this.position = position;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
		TextView textView = (TextView) rootView.findViewById(R.id.item);
		textView.setText("View " + position);
		
		return rootView;
	}
}