package br.com.casadocodigo.boaviagem;

import static br.com.casadocodigo.boaviagem.Constantes.*;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;

public class BoaViagemActivity extends Activity {

	private SharedPreferences preferencias;
	private GoogleAccountManager accountManager;
	private Account conta;
	private EditText usuario;
	private EditText senha;
	private CheckBox manterConectado;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        accountManager = new GoogleAccountManager(this);
        
        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
        manterConectado = (CheckBox) findViewById(R.id.manterConectado);
        
        preferencias = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
        boolean conectado = preferencias.getBoolean(MANTER_CONECTADO, false);

        if(conectado){
        	solicitarAutorizacao();
        }
    }
	
	private void iniciarDashboard(){
		startActivity(new Intent(this, DashboardActivity.class));
	}
	
	public void entrarOnClick(View v){
    	String usuarioInformado = usuario.getText().toString();
    	String senhaInformada = senha.getText().toString();
    	
    	autenticar(usuarioInformado, senhaInformada);
    }
	
	private void autenticar(final String nomeConta, String senha) {

		conta = accountManager.getAccountByName(nomeConta);
		
		if(conta == null){
			Toast.makeText(this, R.string.conta_inexistente, Toast.LENGTH_LONG).show();
			return;
		}
		
		Bundle bundle = new Bundle();
		bundle.putString(AccountManager.KEY_ACCOUNT_NAME, nomeConta);
		bundle.putString(AccountManager.KEY_PASSWORD, senha);
		
		accountManager.getAccountManager()
						.confirmCredentials(conta , bundle, this, 
										new AutenticacaoCallback(), null);
	}
	
	private void solicitarAutorizacao() {

		String tokenAcesso = preferencias.getString(TOKEN_ACESSO, null);
		String nomeConta = preferencias.getString(NOME_CONTA, null);
		
		if(tokenAcesso != null){
			accountManager.invalidateAuthToken(tokenAcesso);
			conta = accountManager.getAccountByName(nomeConta);
		}
		
		accountManager.getAccountManager()
				.getAuthToken(conta,
				Constantes.AUTH_TOKEN_TYPE,
				null,
				this,
				new AutorizacaoCallback(),
				null);
	}
	
	private class AutorizacaoCallback implements AccountManagerCallback<Bundle> {

		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			
			try {
				Bundle bundle = future.getResult();
				String nomeConta = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
				String tokenAcesso = bundle.getString(AccountManager.KEY_AUTHTOKEN);

				gravarTokenAcesso(nomeConta, tokenAcesso);
				
				iniciarDashboard();

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
		editor.putBoolean(MANTER_CONECTADO, manterConectado.isChecked());
		editor.commit();
	}
	
	private class AutenticacaoCallback implements AccountManagerCallback<Bundle>{
		@Override
		public void run(AccountManagerFuture<Bundle> future) {

			try {

				Bundle bundle = future.getResult();
				if(bundle.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)){
					
					solicitarAutorizacao();
					
				}else{
					Toast.makeText(getBaseContext(), 
							getString(R.string.erro_autenticacao), 
							Toast.LENGTH_LONG).show();
				}

			} catch (OperationCanceledException e) {
				//usuário cancelou a operação
			} catch (AuthenticatorException e) {
				//possível problema no autenticador
			} catch (IOException e) {
				//possível problema de comunicação
			}
		}
	}
}
