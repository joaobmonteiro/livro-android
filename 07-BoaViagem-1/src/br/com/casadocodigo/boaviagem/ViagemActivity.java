package br.com.casadocodigo.boaviagem;

import static br.com.casadocodigo.boaviagem.Constantes.NOME_CONTA;
import static br.com.casadocodigo.boaviagem.Constantes.PREFERENCIAS;
import static br.com.casadocodigo.boaviagem.Constantes.TOKEN_ACESSO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import br.com.casadocodigo.boaviagem.calendar.CalendarService;
import br.com.casadocodigo.boaviagem.dao.BoaViagemDAO;
import br.com.casadocodigo.boaviagem.domain.Viagem;

public class ViagemActivity extends Activity {

	private Date dataChegada, dataSaida;
	private int ano, mes, dia;
	private Button dataChegadaButton, dataSaidaButton;
	private EditText destino, quantidadePessoas, orcamento;
	private RadioGroup radioGroup;
	private Long id;
	private BoaViagemDAO dao;
	private CalendarService calendarService;

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
		
		destino = (EditText) findViewById(R.id.destino);
		quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
		orcamento = (EditText) findViewById(R.id.orcamento);
		radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);
		
		dao = new BoaViagemDAO(this);
		
		id = getIntent().getLongExtra(Constantes.VIAGEM_ID, -1);
		
		if(id != -1){
			prepararEdicao();
		}
		
		calendarService = criarCalendarService();
	}

	private CalendarService criarCalendarService() {
		SharedPreferences preferencias = 
						getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
		String nomeConta = preferencias.getString(NOME_CONTA, null);
		String tokenAcesso = preferencias.getString(TOKEN_ACESSO, null);
		
		return new CalendarService(nomeConta, tokenAcesso);
	}

	private void prepararEdicao() {
		Viagem viagem = dao.buscarViagemPorId(id);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		if(viagem.getTipoViagem() == Constantes.VIAGEM_LAZER){
			radioGroup.check(R.id.lazer);
		}else{
			radioGroup.check(R.id.negocios);
		}

		destino.setText(viagem.getDestino());
		dataChegada = viagem.getDataChegada();
		dataSaida = viagem.getDataSaida();
		dataChegadaButton.setText(dateFormat.format(dataChegada));
		dataSaidaButton.setText(dateFormat.format(dataSaida));
		quantidadePessoas.setText(viagem.getQuantidadePessoas().toString());
		orcamento.setText(viagem.getOrcamento().toString());
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
			removerViagem(Long.valueOf(id));
			finish();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}
	
	private void removerViagem(Long id) {
		dao.removerViagem(id);
		dao.removerGastosViagem(id);
	}
	
	public void salvarViagem(View view){
		
		Viagem viagem = new Viagem();
		viagem.setDestino(destino.getText().toString());
		viagem.setDataChegada(dataChegada);
		viagem.setDataSaida(dataSaida);
		viagem.setOrcamento(
				Double.valueOf(orcamento.getText().toString()));
		viagem.setQuantidadePessoas(
				Integer.valueOf(quantidadePessoas.getText().toString()));
		
		int tipo = radioGroup.getCheckedRadioButtonId();
		
		if(tipo == R.id.lazer){
			viagem.setTipoViagem(Constantes.VIAGEM_LAZER);
		}else{
			viagem.setTipoViagem(Constantes.VIAGEM_NEGOCIOS);
		}
		
		long resultado;
		
		if(id == -1){
			resultado = dao.inserir(viagem);
			new Task().execute(viagem);
		}else{
			resultado = dao.atualizar(viagem);
		}
		
		if(resultado != -1 ){
			Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		dao.close();
		super.onDestroy();
	}
	
	private class Task extends AsyncTask<Viagem, Void, Void>{
		@Override
		protected Void doInBackground(Viagem... viagens) {
			Viagem viagem = viagens[0];
			calendarService.criarEvento(viagem);
			return null;
		}
	}
}