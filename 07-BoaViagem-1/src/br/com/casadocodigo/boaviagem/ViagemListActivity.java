package br.com.casadocodigo.boaviagem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import br.com.casadocodigo.boaviagem.dao.BoaViagemDAO;
import br.com.casadocodigo.boaviagem.domain.Viagem;

public class ViagemListActivity extends ListActivity implements
		OnItemClickListener, OnClickListener, ViewBinder {

	private List<Map<String, Object>> viagens;
	private AlertDialog alertDialog;
	private AlertDialog dialogConfirmacao;
	private int viagemSelecionada;
	private boolean modoSelecionarViagem;
	private SimpleDateFormat dateFormat;
	private Double valorLimite;
	private BoaViagemDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new BoaViagemDAO(this);
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		SharedPreferences preferencias = 
						   PreferenceManager.getDefaultSharedPreferences(this);
		String valor = preferencias.getString("valor_limite", "-1");
		valorLimite = Double.valueOf(valor);
		
		getListView().setOnItemClickListener(this);
		alertDialog = criarAlertDialog();
		dialogConfirmacao = criarDialogConfirmacao();

		if (getIntent().hasExtra(Constantes.MODO_SELECIONAR_VIAGEM)) {
			modoSelecionarViagem = 
					getIntent().getExtras()
							   .getBoolean(Constantes.MODO_SELECIONAR_VIAGEM);
		}
		
		//TODO mostrar textview informando que os registros estão sendo carregados
		
		new Task().execute(null);
		
	}
	
	private class Task extends AsyncTask<Void, Void, List<Map<String, Object>>>{
		@Override
		protected List<Map<String, Object>> doInBackground(Void... params) {
			return listarViagens();
		}
		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			String[] de = { "imagem", "destino", "data", 
					"total", "barraProgresso" };
	
			int[] para = { R.id.tipoViagem, R.id.destino, 
				   R.id.data, R.id.valor,
				   R.id.barraProgresso };
	
			SimpleAdapter adapter = new SimpleAdapter(ViagemListActivity.this, result, R.layout.lista_viagem, de, para);

			adapter.setViewBinder(ViagemListActivity.this);
			
			setListAdapter(adapter);
		}
	}

	private List<Map<String, Object>> listarViagens() {

		viagens = new ArrayList<Map<String, Object>>();
		
		List<Viagem> listaViagens = dao.listarViagens();

		for (Viagem viagem : listaViagens) {

			Map<String, Object> item = new HashMap<String, Object>();

			item.put("id", viagem.getId());

			if (viagem.getTipoViagem() == Constantes.VIAGEM_LAZER) {
				item.put("imagem", R.drawable.lazer);
			} else {
				item.put("imagem", R.drawable.negocios);
			}

			item.put("destino", viagem.getDestino());

			String periodo = dateFormat.format(viagem.getDataChegada()) + " a "
								+ dateFormat.format(viagem.getDataSaida());

			item.put("data", periodo);

			double totalGasto = dao.calcularTotalGasto(viagem);

			item.put("total", "Gasto total R$ " + totalGasto);

			double alerta = viagem.getOrcamento() * valorLimite / 100;
			Double [] valores = new Double[] { viagem.getOrcamento(), alerta, totalGasto };
			item.put("barraProgresso", valores);
			viagens.add(item);

		}
		return viagens;
	}

	@Override
	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		if (view.getId() == R.id.barraProgresso) {
			Double valores[] = (Double[]) data;
			ProgressBar progressBar = (ProgressBar) view;
			progressBar.setMax(valores[0].intValue());
			progressBar.setSecondaryProgress(valores[1].intValue());
			progressBar.setProgress(valores[2].intValue());
			return true;
		}
		return false;
	}

	private AlertDialog criarDialogConfirmacao() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirmacao_exclusao_viagem);
		builder.setPositiveButton(getString(R.string.sim), this);
		builder.setNegativeButton(getString(R.string.nao), this);

		return builder.create();
	}

	private AlertDialog criarAlertDialog() {
		final CharSequence[] items = { getString(R.string.editar),
				getString(R.string.novo_gasto),
				getString(R.string.gastos_realizados),
				getString(R.string.remover) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.opcoes);
		builder.setItems(items, this);

		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int item) {
		Intent intent;
		Long id = (Long) viagens.get(viagemSelecionada).get("id");
		
		switch (item) {
		case 0: //editar viagem
			intent = new Intent(this, ViagemActivity.class);
			intent.putExtra(Constantes.VIAGEM_ID, id);
			startActivity(intent);
			break;
		case 1: //novo gasto
			String destino = viagens.get(viagemSelecionada)
									.get("destino").toString();
			
			intent = new Intent(this, GastoActivity.class);
			intent.putExtra(Constantes.VIAGEM_ID, id);
			intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
			startActivity(intent);
			break;
		case 2: //lista de gastos realizados
			intent = new Intent(this, GastoListActivity.class);
			intent.putExtra(Constantes.VIAGEM_ID, id);
			startActivity(intent);
			break;
		case 3: //confirmacao de exclusao
			dialogConfirmacao.show();
			break;
		case DialogInterface.BUTTON_POSITIVE: //exclusao
			viagens.remove(viagemSelecionada);
			dao.removerViagem(Long.valueOf(id));
			getListView().invalidateViews();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialogConfirmacao.dismiss();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (modoSelecionarViagem) {
			String destino = (String) viagens.get(position).get("destino");
			String idViagem = (String) viagens.get(position).get("id");

			Intent data = new Intent();
			data.putExtra(Constantes.VIAGEM_ID, idViagem);
			data.putExtra(Constantes.VIAGEM_DESTINO, destino);
			setResult(Activity.RESULT_OK, data);
			finish();
		} else {
			viagemSelecionada = position;
			alertDialog.show();
		}
	}
	
	@Override
	protected void onDestroy() {
		dao.close();
		super.onDestroy();
	}
}


