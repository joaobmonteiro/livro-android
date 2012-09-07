package br.com.casadocodigo.livrodeandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HelloAndroidActivity extends Activity {
    private EditText nomeEditText;
	private TextView saudacaoTextView;
	private String saudacao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_android);
        
        nomeEditText = (EditText) findViewById(R.id.nomeEditText);
    	saudacaoTextView = (TextView) findViewById(R.id.saudacaoTextView);
    	saudacao = getResources().getString(R.string.saudacao);
    }
    
    public void surpreenderUsuario(View v){
    	saudacaoTextView.setText(saudacao + " " + nomeEditText.getText());
    }
}