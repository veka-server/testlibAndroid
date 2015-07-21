package vekaUtils;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.util.Log;

public class Audio {
	
	private static MediaRecorder recorder;
	
    private static long startTime, stopTime = 0L;
    private static Boolean RecordInProgress = false;

    /**
     * Get intent to call an activity for recoding audio message
     * @param c context 
     * @param outputFilePath path for output file
     * @return intent
     */
    public static Intent intentGetAudio(Context c, String outputFilePath ) {

		Intent i = new Intent(c,com.vekandroid.vekautils.RecAudio.class);
		i.putExtra("output", outputFilePath); 		
		return i;
    }
    
    /**
     * Check if an record is in progress
     * @return true if in progress
     */
    public static Boolean isRecordInProgress() {
		return RecordInProgress;
	}

	/**
     * Start an audio record
     * @param file Path for save the audio file
     */
	public static void startRecording(String file) {
		
		if(RecordInProgress)
			return ;
		RecordInProgress = true;
		
		recorder = new MediaRecorder();

		// source
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		
		// format audio
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		// choix du fichier
		recorder.setOutputFile(file);

		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			Log.e("StartRecording", "IllegalStateException " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("StartRecording", "IOException " + e.getMessage());
			e.printStackTrace();
		}
		recorder.start();
		startTime = SystemClock.uptimeMillis();
	}
	
	/**
	 * Stop the current record (millisecond)
	 */
	public static void stopRecording() {

		if(!RecordInProgress)
			return ;
		RecordInProgress = false;

		stopTime = SystemClock.uptimeMillis();

		recorder.stop();
		recorder.reset();   // You can reuse the object by going back to setAudioSource() step
		recorder.release(); // Now the object cannot be reused
	}

	/**
	 * Get duration of current record
	 * @return long duration of current record in seconds
	 */
	public static long getDurationOfRecord() {
		if(RecordInProgress)
			return SystemClock.uptimeMillis() - startTime;
		else
			return stopTime - startTime;
	}
	
	/**
	 * Play an audio file
	 * @param context Context of the application
	 * @param fichier Path of the audio file
	 * @return MediaPlayer
	 */
	public static MediaPlayer playFile(Context context , String fichier) {

		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(fichier);
			player.prepare();
			player.start();
		} catch (IOException e) {
			Log.e("StartPlaying", "IOException " + e.getMessage());
			e.printStackTrace();
		}
		return player;
	}
		


}
