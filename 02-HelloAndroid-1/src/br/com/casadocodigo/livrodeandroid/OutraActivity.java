package br.com.casadocodigo.livrodeandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OutraActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hello_android);
	}
	
	public void abrirAtividade(View v){
		   Intent intent = new Intent(this, OutraActivity.class);
		   startActivity(intent);
	   }
}
