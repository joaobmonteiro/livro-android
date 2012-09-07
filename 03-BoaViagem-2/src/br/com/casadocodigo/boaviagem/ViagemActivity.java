package br.com.casadocodigo.boaviagem;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class ViagemActivity extends Activity {

	private Date dataChegada, dataSaida;
	private int ano, mes, dia;
	private Button dataChegadaButton, dataSaidaButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viagem);

		Calendar calendar = Calendar.getInstance();
		ano = calendar.get(Calendar.YEAR);
		mes = calendar.get(Calendar.MONTH);
		dia = calendar.get(Calendar.DAY_OF_MONTH);
		
		dataChegadaButton = (Button) findViewById(R.id.dataChegada);
		dataSaidaButton = (Button) findViewById(R.id.dataSaida);
		
	}

	public void selecionarData(View view) {
		showDialog(view.getId());
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case R.id.dataChegada:
			return new DatePickerDialog(this, dataChegadaListener, ano, mes, dia);

		case R.id.dataSaida:
			return new DatePickerDialog(this, dataSaidaListener, ano, mes, dia);
		}
		return null;
	}
	
	private OnDateSetListener dataChegadaListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
			dataChegada = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
			dataChegadaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
		}
	};

	private OnDateSetListener dataSaidaListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int anoSelecionado, int mesSelecionado, int diaSelecionado) {
			dataSaida = criarData(anoSelecionado, mesSelecionado, diaSelecionado);
			dataSaidaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
		}
	};

	private Date criarData(int anoSelecionado, int mesSelecionado, int diaSelecionado) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(anoSelecionado, mesSelecionado, diaSelecionado);
		return calendar.getTime();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.viagem_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.novo_gasto:
			startActivity(new Intent(this, GastoActivity.class));
			return true;
		case R.id.remover:
			//remover viagem do banco de dados
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}
}
