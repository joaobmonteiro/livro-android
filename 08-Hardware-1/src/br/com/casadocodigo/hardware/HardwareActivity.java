package br.com.casadocodigo.hardware;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HardwareActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void escolherOpcao(View view){
    	if(view.getId() == R.id.camera){
    		Intent intent = new Intent(this, CameraActivity.class);
    		startActivity(intent);
    	}
    	
    	if(view.getId() == R.id.localizacao){
    		Intent intent = new Intent(this, LocalizacaoActivity.class);
    		startActivity(intent);
    	}
    	
    	if(view.getId() == R.id.mediaplayer){
    		Intent intent = new Intent(this, MediaPlayerActivity.class);
    		startActivity(intent);
    	}
    	
    }
}