package edu.upenn.cis573;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.DONUT)
@SuppressLint("NewApi")
public class Help extends Activity implements OnInitListener{
	
	private TextToSpeech tts;
	private boolean muteSpeaker = true;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);	
		tts = new TextToSpeech(this, this);
		
		// get action bar   
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.menu, menu);
		inflater.inflate(R.menu.help_menu, menu);
		setTitle("StudySpaces");
		getActionBar().setDisplayShowTitleEnabled(true);
		return true;
	}

	public boolean onSpeakerOnClick(MenuItem menu) {
		muteSpeaker = false;
		startSpeaking();
		return true;
	}
	
	public boolean onSpeakerOffClick(MenuItem menu) {
		
		if (tts.isSpeaking()) 
			tts.stop();
		muteSpeaker = true;
		return true;
	}
	
	public void startSpeaking() {
		if (tts != null) {
			
			String text = "Welcome to Penn Study Spaces";
			System.out.println(text);
			text += getResources().getString(R.string.search_screen_help);
			text += getResources().getString(R.string.main_screen_help);
			text += getResources().getString(R.string.study_space_info_help);
			text += getResources().getString(R.string.map_screen_help);
			
			if (text != null) {
				if (!tts.isSpeaking())
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	
			}
		}	
	}
	

	@TargetApi(Build.VERSION_CODES.DONUT)
	@SuppressLint("NewApi")
	@Override
	public void onInit(int code) {
		if (code==TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.getDefault());
		} else {
			tts = null;
			Toast.makeText(this, "Failed to initialize TTS engine.", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		if (tts!=null) {
			tts.stop();
            tts.shutdown();
		}
		super.onDestroy();
	}
	
	public void stopVoice()
	{
		if (tts.isSpeaking())
				tts.stop();
	}
}
