package br.com.casadocodigo.livrodeandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	public void abrirHelloAndroid(View v) {
		Intent intent = new Intent(this, HelloAndroidActivity.class);
		startActivity(intent);
	}
	
	public void abrirIntents(View v) {
		Intent intent = new Intent(this, IntentsActivity.class);
		startActivity(intent);
	}
}