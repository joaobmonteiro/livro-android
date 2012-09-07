package br.com.casadocodigo.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HelloAndroidActivity extends Activity {
    private EditText nomeEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        nomeEditText = (EditText) findViewById(R.id.nomeEditText);
    }
    
    public void surpreenderUsuario(View v){
    	Intent intent = new Intent(SaudacaoActivity.ACAO_EXIBIR_SAUDACAO);
    	intent.addCategory(SaudacaoActivity.CATEGORIA_SAUDACAO);
    	intent.putExtra(SaudacaoActivity.EXTRA_NOME_USUARIO, nomeEditText.getText().toString());
    	startActivity(intent);
    }
}