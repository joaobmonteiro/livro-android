package br.com.casadocodigo.hardware;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class MediaPlayerActivity extends Activity 
									implements OnPreparedListener{

	private MediaPlayer player;
	private VideoView videoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player);
		videoView = (VideoView) findViewById(R.id.videoView);
	}
	
	public void executarVideo(View v) {
		Uri uri = Uri.parse("android.resource://"+ getPackageName() + 
							"/" + R.raw.video);
		
		videoView.setVideoURI(uri);
		MediaController mc = new MediaController(this);
		videoView.setMediaController(mc);
		videoView.start();
	}

	public void executarMusicaArquivo(View v) {
		player = MediaPlayer.create(this, R.raw.musica);
		player.start();
	}
	
	public void executar(View v) {
		if(!player.isPlaying()){
			player.start();
		}
	}

	public void pausar(View v) {
		if(player.isPlaying()){
			player.pause();
		}
	}
	
	public void parar(View v) {
		if(player.isPlaying()){
			player.stop();
		}
	}
	
	public void executarMusicaUrl(View v) {
		liberarPlayer();
		player = new MediaPlayer();
		try {
			Uri uri = Uri.parse("http://<alguma-url>/musica.mp3");
			player.setDataSource(this, uri);
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setOnPreparedListener(this);
			player.prepareAsync();
		} catch (Exception e) {}
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		player.start();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		liberarPlayer();
	}
	
	private void liberarPlayer() {
		if(player != null){
			player.release();
		}
	}
}