package br.com.casadocodigo.twittersearch;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.kevinsawicki.http.HttpRequest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

public class NotificacaoService extends Service {
	
	private String accessToken;
	
	private class AutenticacaoTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Map<String, String> data = new HashMap<String, String>();
				data.put("grant_type", "client_credentials");
				String json = HttpRequest
						.post("https://api.twitter.com/oauth2/token")
						.authorization("Basic "+ gerarChave())
						.form(data)
						.body();

				JSONObject token = new JSONObject(json);
				accessToken = token.getString("access_token");
			} catch (Exception e) {
				return null;
			}
			return null;
		}
		
		private String gerarChave() throws UnsupportedEncodingException{
			String key = "key";
			String secret = "secret";
			String token = key + ":" + secret;
			String base64 = Base64.encodeToString(token.getBytes(), Base64.NO_WRAP);
			return base64;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
			long delayInicial = 0;
			long periodo = 10;
			TimeUnit unit = TimeUnit.MINUTES;
			pool.scheduleAtFixedRate(new NotificacaoTask(), delayInicial, periodo, unit);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new AutenticacaoTask().execute();
		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
		long delayInicial = 0;
		long periodo = 10;
		TimeUnit unit = TimeUnit.MINUTES;
		pool.scheduleAtFixedRate(new NotificacaoTask(), delayInicial, periodo, unit);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private boolean estaConectado() {
		ConnectivityManager manager = 
			(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		Log.i(getPackageName(), "conectado " + info.isConnected());
		return info.isConnected();
	}

	private class NotificacaoTask implements Runnable {
		private String baseUrl = "https://api.twitter.com/1.1/search/tweets.json";
		private String refreshUrl = "?q=@android";

		@Override
		public void run() {
			if (!estaConectado()) {
				return;
			}
			try {
				String conteudo = HttpRequest.get(baseUrl + refreshUrl)
						.authorization("Bearer " + accessToken)
						.body();

				JSONObject jsonObject = new JSONObject(conteudo);
				refreshUrl = jsonObject.getJSONObject("search_metadata")
									   .getString("refresh_url");

				JSONArray resultados = jsonObject.getJSONArray("statuses");

				for (int i = 0; i < resultados.length(); i++) {
					JSONObject tweet = resultados.getJSONObject(i);
					String texto = tweet.getString("text");
					String usuario = tweet.getJSONObject("user").getString("screen_name");
					criarNotificacao(usuario, texto, i);
				}
			} catch (Exception e) {
				Log.e(getPackageName(), e.getMessage(), e);
			}
		}

		private void criarNotificacao(String usuario, String texto, int id) {
			int icone = R.drawable.ic_launcher;
			String aviso = getString(R.string.aviso);
			long data = System.currentTimeMillis();
			String titulo = usuario + " " + getString(R.string.titulo);
			
			Context context = getApplicationContext();
			Intent intent = new Intent(context, TweetActivity.class);
			intent.putExtra(TweetActivity.USUARIO, usuario.toString());
			intent.putExtra(TweetActivity.TEXTO, texto.toString());
			
			PendingIntent pendingIntent = 
					PendingIntent.getActivity(context, id, intent, 
									Intent.FLAG_ACTIVITY_NEW_TASK);

			Notification notification = new Notification(icone, aviso, data);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults |= Notification.DEFAULT_VIBRATE; 
			notification.defaults |= Notification.DEFAULT_LIGHTS; 
			notification.defaults |= Notification.DEFAULT_SOUND;
			
			notification.setLatestEventInfo(context, titulo, 
											texto, pendingIntent);
			
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager notificationManager = 
					(NotificationManager) getSystemService(ns);
			notificationManager.notify(id, notification);
		}
	}
}