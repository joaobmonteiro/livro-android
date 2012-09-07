package br.com.casadocodigo.twittersearch;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TwitterSearchActivity extends Activity {

	private ListView lista;
	private EditText texto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		lista = (ListView) findViewById(R.id.lista);
		texto = (EditText) findViewById(R.id.texto);
	}

	public void buscar(View v) {
		String filtro = texto.getText().toString();
		new TwitterTask().execute(filtro);
		startService(new Intent(this,NotificacaoService.class));
	}
	
	private class TwitterTask extends AsyncTask<String, Void, String[]>{

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(TwitterSearchActivity.this);
			dialog.setMessage("Aguarde");
			dialog.show();
		}

		@Override
		protected String[] doInBackground(String... params) {
			try {

				String filtro = params[0];

				if(TextUtils.isEmpty(filtro)){
					return null;
				}

				String urlTwitter = "http://search.twitter.com/search.json?q=";

				String url = Uri.parse(urlTwitter + filtro).toString();
				
				Log.i(getPackageName(), url);

				String conteudo = HTTPUtils.acessar(url);

				JSONObject jsonObject = new JSONObject(conteudo);

				JSONArray resultados = jsonObject.getJSONArray("results");

				String[] tweets = new String[resultados.length()];

				for (int i = 0; i < resultados.length(); i++) {
					JSONObject tweet = resultados.getJSONObject(i);
					String texto = tweet.getString("text");
					String usuario = tweet.getString("from_user");
					tweets[i] = usuario + " - " + texto;
				}

				return tweets;

			} catch (Exception e) {
				Log.e(getPackageName(), e.getMessage(), e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(String[] result) {
			if(result != null){
				ArrayAdapter<String> adapter = 
						new ArrayAdapter<String>(getBaseContext(), 
								android.R.layout.simple_list_item_1, result);

				lista.setAdapter(adapter);
			}
			dialog.dismiss();
		}
	}
}