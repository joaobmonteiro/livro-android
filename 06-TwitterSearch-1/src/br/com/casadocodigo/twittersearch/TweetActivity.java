package br.com.casadocodigo.twittersearch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TweetActivity extends Activity {
	public static final String TEXTO = "texto";
	public static final String USUARIO = "usuario";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweet);
		
		TextView usuarioTextView = (TextView) findViewById(R.id.usuario);
		TextView textoTextView = (TextView) findViewById(R.id.texto);
		
		String usuario = getIntent().getStringExtra(USUARIO);
		String texto = getIntent().getStringExtra(TEXTO);
		
		usuarioTextView.setText(usuario);
		textoTextView.setText(texto);
	}
}
