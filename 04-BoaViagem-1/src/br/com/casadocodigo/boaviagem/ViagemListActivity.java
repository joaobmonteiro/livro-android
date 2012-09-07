package br.com.casadocodigo.boaviagem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class ViagemListActivity extends ListActivity implements
		OnItemClickListener, OnClickListener, ViewBinder {

	private List<Map<String, Object>> viagens;
	private AlertDialog alertDialog;
	private AlertDialog dialogConfirmacao;
	private int viagemSelecionada;
	private boolean modoSelecionarViagem;
	private DatabaseHelper helper;
	private SimpleDateFormat dateFormat;
	private Double valorLimite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		helper = new DatabaseHelper(this);
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

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor =
				db .rawQuery("SELECT _ID, TIPO_VIAGEM, DESTINO, " +
							"DATA_CHEGADA, DATA_SAIDA, ORCAMENTO FROM VIAGEM",
							null);
		
		cursor.moveToFirst();

		viagens = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < cursor.getCount(); i++) {

			Map<String, Object> item = new HashMap<String, Object>();

			String id = cursor.getString(0);
			int tipoViagem = cursor.getInt(1);
			String destino = cursor.getString(2);
			long dataChegada = cursor.getLong(3);
			long dataSaida = cursor.getLong(4);
			double orcamento = cursor.getDouble(5);

			item.put("id", id);

			if (tipoViagem == Constantes.VIAGEM_LAZER) {
				item.put("imagem", R.drawable.lazer);
			} else {
				item.put("imagem", R.drawable.negocios);
			}

			item.put("destino", destino);

			Date dataChegadaDate = new Date(dataChegada);
			Date dataSaidaDate = new Date(dataSaida);

			String periodo = dateFormat.format(dataChegadaDate) + " a "
								+ dateFormat.format(dataSaidaDate);

			item.put("data", periodo);

			double totalGasto = calcularTotalGasto(db, id);

			item.put("total", "Gasto total R$ " + totalGasto);

			double alerta = orcamento * valorLimite / 100;
			Double [] valores = new Double[] { orcamento, alerta, totalGasto };
			item.put("barraProgresso", valores);
			viagens.add(item);

			cursor.moveToNext();
		}
		cursor.close();
		db.close();

		return viagens;
	}

	private double calcularTotalGasto(SQLiteDatabase db, String id) {
		Cursor cursor = db.rawQuery("SELECT SUM(VALOR) FROM GASTO WHERE VIAGEM_ID = ?", 
									new String[]{ id });
		cursor.moveToFirst();
		double total = cursor.getDouble(0);
		cursor.close();
		return total;
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
		String id = (String) viagens.get(viagemSelecionada).get("id");
		
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
			removerViagem(id);
			getListView().invalidateViews();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialogConfirmacao.dismiss();
			break;
		}
	}

	private void removerViagem(String id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String where []  = new String[]{id};
		db.delete("GASTO", "VIAGEM_ID = ?", where);
		db.delete("VIAGEM", "_ID = ?", where);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (modoSelecionarViagem) {
			String destino = (String) viagens.get(position).get("destino");
			Integer idViagem = (Integer) viagens.get(position).get("id");

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
	public Cursor queryAPI(){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		String tabela = "VIAGEM";
		String[] colunas = new String[]{"_ID", "TIPO_VIAGEM", "DESTINO", 
										"DATA_CHEGADA","DATA_SAIDA", 
										"ORCAMENTO"};
		String selecao = null;
		String[] selecaoArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		Cursor cursor = db.query(tabela, colunas, selecao, 
								 selecaoArgs, groupBy,
								 having, orderBy);
		
		return cursor;
	}
	
	public Cursor queryBuilder(){
		
		String tabela = "VIAGEM";
		String[] colunas = new String[]{"_ID", "TIPO_VIAGEM", "DESTINO", 
										"DATA_CHEGADA","DATA_SAIDA", 
										"ORCAMENTO"};
		String selecao = null;
		String[] selecaoArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		
		SQLiteDatabase db = helper.getReadableDatabase();
		
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		
		builder.setTables("VIAGEM");
		
		Cursor cursor = builder.query(db, colunas, selecao, 
				 					 selecaoArgs, groupBy,
				 					 having, orderBy);
		
		return cursor;
	}
	
	@Override
	protected void onDestroy() {
		helper.close();
		super.onDestroy();
	}
}
