package br.com.casadocodigo.livrodeandroid;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class IntentsActivity extends Activity {
	
	private String pastaImagens;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intents);
		pastaImagens = "/sdcard/LivroDeAndroid/";
		File file = new File(pastaImagens);
		if(!file.exists()){
			file.mkdir();
		}
	}

	public void abrirNavegador(View v){
		Uri uri = Uri.parse("http://www.android.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void abrirContatos(View v){
		Uri uri = Uri.parse("content://contacts/people/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void abrirAtividade(View v) {
		Intent intent = new Intent(this, OutraActivity.class);
		startActivity(intent);
	}

	public void abrirCamera(View v) {
		String nome = "img_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
		File arquivo = new File(pastaImagens + nome);
		Uri uri = Uri.fromFile(arquivo);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivity(intent);
	}
}
