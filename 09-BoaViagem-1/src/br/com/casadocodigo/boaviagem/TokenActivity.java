package br.com.casadocodigo.boaviagem;

import static br.com.casadocodigo.boaviagem.Constantes.NOME_CONTA;
import static br.com.casadocodigo.boaviagem.Constantes.TOKEN_ACESSO;

import java.io.IOException;
import java.util.Date;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import br.com.casadocodigo.boaviagem.calendar.CalendarService;
import br.com.casadocodigo.boaviagem.domain.Viagem;

import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;

public class TokenActivity extends Activity{

	private CalendarService service;
	public String tokenAcesso;
	public String nomeConta;
	private SharedPreferences preferencias;
	private GoogleAccountManager accountManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.token);

		preferencias = getSharedPreferences(Constantes.PREFERENCIAS, MODE_PRIVATE);
		tokenAcesso = preferencias.getString(Constantes.TOKEN_ACESSO, null);
		nomeConta = preferencias.getString(Constantes.NOME_CONTA, null);
		
		accountManager = new GoogleAccountManager(this);
	}
	
	public void onClick(View view){

		Account conta = accountManager.getAccountByName(nomeConta);
		
		accountManager.invalidateAuthToken(tokenAcesso);
		
		accountManager.getAccountManager()
		.getAuthToken(conta,
		Constantes.AUTH_TOKEN_TYPE,
		null,
		this,
		new AutorizacaoCallback(),
		null);
		
		//Log.i(getPackageName(), "Validade do token " + credencial.getExpirationTimeMilliseconds());
		//Log.i(getPackageName(), "Tempo para expirar " + credencial.getExpiresInSeconds());
	}
	
	private class AutorizacaoCallback implements AccountManagerCallback<Bundle> {

		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			
			try {
				Bundle bundle = future.getResult();
				nomeConta = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
				tokenAcesso = bundle.getString(AccountManager.KEY_AUTHTOKEN);
				
				gravarTokenAcesso(nomeConta, tokenAcesso);
				
				service = new CalendarService(nomeConta, tokenAcesso);
				
				Date data = new Date();
				Viagem viagem = new Viagem();
				viagem.setDestino("Teste " + data);
				viagem.setDataChegada(data);
				viagem.setDataSaida(new Date(data.getTime() + 360000));
				
				String id = service.criarEvento(viagem);
				
				Log.i(getPackageName(), "id do evento " + id);

			} catch (OperationCanceledException e) {
				//usuário cancelou a operação
			} catch (AuthenticatorException e) {
				//possível problema no autenticador
			} catch (IOException e) {
				//possível problema de comunicação
			}
		}
	}
	
	private void gravarTokenAcesso(String nomeConta, String tokenAcesso) {
		Editor editor = preferencias.edit();
		editor.putString(NOME_CONTA, nomeConta);
		editor.putString(TOKEN_ACESSO, tokenAcesso);
		editor.commit();
	}
}

