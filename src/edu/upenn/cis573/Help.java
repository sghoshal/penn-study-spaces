package edu.upenn.cis573;

import java.io.File;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.AssetManager;
import android.renderscript.Element;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.DONUT)
@SuppressLint("NewApi")
public class Help extends Activity implements OnClickListener, OnInitListener{
	
	private TextToSpeech tts;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);	
		tts = new TextToSpeech(this, this);
		findViewById(R.id.voiceHelpButton).setOnClickListener(this);
	}

	@TargetApi(Build.VERSION_CODES.DONUT)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (tts!=null) {
			
			String text = "Welcome to Penn Study Spaces";
			System.out.println(text);
			text += getResources().getString(R.string.search_screen_help);
			text += getResources().getString(R.string.main_screen_help);
			text += getResources().getString(R.string.study_space_info_help);
			text += getResources().getString(R.string.map_screen_help);
			
			if (text!=null) {
				if (!tts.isSpeaking()) {
					tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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
