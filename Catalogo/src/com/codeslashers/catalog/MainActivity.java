package com.codeslashers.catalog;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnQueryTextListener{
	private ShareActionProvider shareActionProvider;
	
	private List<String> products = 
			Arrays.asList("android", "ios", "mobile", "java", "agile");
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        shareActionProvider.setShareIntent(getShareIntent());
        
        return true;
    }

    private Intent getShareIntent() {
    	 	Intent intent = new Intent(Intent.ACTION_SEND);
    	    intent.setType("text/html");
    	    intent.putExtra(Intent.EXTRA_TEXT, getInformation());
    	    return intent;
	}
    
    private String getInformation(){
    		
    		return "Information for sharing";
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_checkout){
        		Toast.makeText(this, getString(R.string.action_checkout), 
        								Toast.LENGTH_SHORT).show();
        }
        
        if(id == R.id.action_filter_android) {
        		//search for books in Android category
        		item.setChecked(true);
            return true;
        }
        if(id == R.id.action_filter_mobile) {
	        	//search for books in Mobile category
	        	item.setChecked(true);
	        	return true;
        }
        if(id == R.id.action_filter_java) {
	        	//search for books in Java category
	        	item.setChecked(true);
	        	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

	@Override
	public boolean onQueryTextChange(String novoTexto) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String text) {
		text = text.toLowerCase();
		TextView view = (TextView) getSupportFragmentManager()
				.findFragmentById(R.id.container).getView()
				.findViewById(R.id.search_result);

		for (String name : products) {
			if(name.contains(text)){
				String result = getString(R.string.results_found, text);
				view.setText(result);
				return true;
			}
		}
		
		String result = getString(R.string.results_not_found, text);
		view.setText(result);
		
		return true;
	}
	
	
}
