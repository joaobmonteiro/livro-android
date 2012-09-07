package br.com.casadocodigo.boaviagem.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import br.com.casadocodigo.boaviagem.AnotacaoListener;
import br.com.casadocodigo.boaviagem.Constantes;
import br.com.casadocodigo.boaviagem.provider.BoaViagemContract.Viagem;

public class ViagemListFragment extends ListFragment
								implements	OnItemClickListener,
											LoaderCallbacks<Cursor>{

	private AnotacaoListener callback;
	private SimpleCursorAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null,
				new String[] { Viagem.DESTINO },
				new int[] { android.R.id.text1 }, 0);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, 
							View view, int position, 
							long id) {
		long viagem = getListAdapter().getItemId(position);
		Bundle bundle = new Bundle();
		bundle.putLong(Constantes.VIAGEM_SELECIONADA, viagem);
		callback.viagemSelecionada(bundle);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (AnotacaoListener) activity;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri = Viagem.CONTENT_URI;
		String[] projection = new String[]{Viagem._ID, Viagem.DESTINO};
		return new CursorLoader(getActivity(), uri, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}
}