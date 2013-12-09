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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.DONUT)
@SuppressLint("NewApi")
public class Help extends Activity implements OnClickListener, OnInitListener{
	
	private TextToSpeech tts;
	private Button voiceHelpButton;
	private boolean muteSpeaker = false;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);	
		tts = new TextToSpeech(this, this);
		voiceHelpButton = (Button) findViewById(R.id.voiceHelpButton);
		voiceHelpButton.setOnClickListener(this);
		
		// get action bar   
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@TargetApi(Build.VERSION_CODES.DONUT)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (tts != null) {
			muteSpeaker = !muteSpeaker;
			
			
			
			String text = "Welcome to Penn Study Spaces";
			System.out.println(text);
			text += getResources().getString(R.string.search_screen_help);
			text += getResources().getString(R.string.main_screen_help);
			text += getResources().getString(R.string.study_space_info_help);
			text += getResources().getString(R.string.map_screen_help);
			
			if (text != null) {
				if (!tts.isSpeaking() && muteSpeaker) {
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				}
				else if(tts.isSpeaking() && !muteSpeaker) {
					tts.stop();
				}	
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
