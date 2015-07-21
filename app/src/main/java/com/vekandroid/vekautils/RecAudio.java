package com.vekandroid.vekautils;

import vekaUtils.IO;
import vekaUtils.Time;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class for 
 * @author veka
 *
 */
public class RecAudio extends Activity {

	final Handler handler = new Handler(Looper.getMainLooper());
	private String file, tempFile ;
	private int count = 0;
	private boolean flagAudioPlay = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);

		Bundle extras = getIntent().getExtras();
		file = extras.getString("output");
		tempFile = file+".temp";
	}

	/**
	 * switch record audio ON / OFF
	 * @param v View
	 */
	public void record_btn(View v) {
		if(vekaUtils.Audio.isRecordInProgress())
			stopRecord();
		else
			startRecord();
	}

	
	/**
	 * Start recording audio file
	 */
	private void startRecord() {
		ImageButton button = ((ImageButton) findViewById(R.id.record_btn));
		button.setImageResource(R.drawable.micro_on);
		
		vekaUtils.Audio.startRecording(tempFile);		
		handler.postDelayed(runnable, 1000);

		if(count > 0)
			hidePlay();
		count ++;
		
	}

	/**
	 * Show layout for listening an audio file
	 */
	private void showPlay() {
		LinearLayout blocLecture = ((LinearLayout) findViewById(R.id.blocLecture));
		blocLecture.setVisibility(View.VISIBLE);

		LinearLayout layoutValidate = ((LinearLayout) findViewById(R.id.btnValidate));
		layoutValidate.setVisibility(View.VISIBLE);

//		Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom);
//		button.startAnimation(animation);
	}
	
	/**
	 * hide layout for listening an audio file
	 */
	private void hidePlay() {
		LinearLayout blocLecture = ((LinearLayout) findViewById(R.id.blocLecture));
		blocLecture.setVisibility(View.INVISIBLE);

		LinearLayout layoutValidate = ((LinearLayout) findViewById(R.id.btnValidate));
		layoutValidate.setVisibility(View.INVISIBLE);
		
//		Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom);
//		button.startAnimation(animation);		
	}
	
	/**
	 * Stop audio record
	 */
	private void stopRecord() {
		vekaUtils.Audio.stopRecording();

		ImageButton button = ((ImageButton) findViewById(R.id.record_btn));
		button.setImageResource(R.drawable.micro);
		
		showPlay();
	}

	/**
	 * Runnable thread for update chrono
	 */
	private Runnable runnable = new Runnable() {
	   public void run() {

		   String str = Time.convertHumainReadable(vekaUtils.Audio.getDurationOfRecord()/1000);
		   
			((TextView)findViewById(R.id.textView1)).setText(str);
	        handler.postDelayed(this, 1000);
	   }
	};
	

	/**
	 * Play an audio file
	 * @param v View
	 */
	public void play_btn(View v){
		
		if(flagAudioPlay)
			return;
		
		flagAudioPlay = true;
		
		final MediaPlayer player = vekaUtils.Audio.playFile(this,tempFile);

		Runnable playerAvancement = new Runnable() {
			   public void run() {
				   			   
					String dur = Time.convertHumainReadable(player.getDuration()/1000);
					String curr = Time.convertHumainReadable(player.getCurrentPosition()/1000);
				   
					((TextView)findViewById(R.id.timePlay)).setText(curr+" / "+dur);
					
					if(player.isPlaying())
						handler.postDelayed(this, 100);
					else {
						handler.removeCallbacksAndMessages(null);
						((TextView)findViewById(R.id.timePlay)).setText(dur+" / "+dur);	
						flagAudioPlay = false;
					}

			   }
		};

		handler.postDelayed(playerAvancement, 100);
		
	 }
	
	
	/**
	 * Validate the audio recording file and finish activity.<br/>
	 * The itent return : output = fileNamePath
	 * @param v View
	 */
	public void validate_click(View v) {
		
		if(!IO.moveFile(tempFile, file)){
			Toast.makeText(this, R.string.failMoveFile, Toast.LENGTH_LONG).show();
			return ;
		}

		Intent i = new Intent();
		i.putExtra("output", file);
		setResult(RESULT_OK, i);
		finish();
	}

	/**
	 * Cancel the audio recording file and finish activity.<br/>
	 * The audio file is delete.
	 * @param v
	 */
	public void cancel_click(View v) {	
		IO.deleteFile(tempFile);

		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);
		finish();
	}

	@Override
	public void onBackPressed() {
		cancel_click(null);
	}
		
}
