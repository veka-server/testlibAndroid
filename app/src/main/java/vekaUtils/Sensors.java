package vekaUtils;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;

public class Sensors {

	public static final int PORTRAIT        = Surface.ROTATION_0;
	public static final int PORTRAIT_REV    = Surface.ROTATION_180;
	public static final int LANDSCAPE       = Surface.ROTATION_90;
	public static final int LANDSCAPE_REV   = Surface.ROTATION_270;
	private static int orient   = -1;
	
	private static boolean isListenerEnable = false;

	private static SensorEventListener listener = new SensorEventListener() {
		
		   public void onSensorChanged(SensorEvent event) {

			   		float x = event.values[0];
		            float y   = event.values[1];

		            if (x<5 && x>-5 && y > 5)
		            	Sensors.orient = Sensors.PORTRAIT;
		            else if (x<-5 && y<5 && y>-5)
		            	Sensors.orient = Sensors.LANDSCAPE;
		            else if (x<5 && x>-5 && y<-5)
		            	Sensors.orient = Sensors.PORTRAIT_REV;
		            else if (x>5 && y<5 && y>-5)
		            	Sensors.orient = Sensors.LANDSCAPE_REV;
		            		   
		   }
		
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		
	};
	
	public static int getOrientation( Context c) throws Exception {
		
		startListenerDeviceOrientation(c);
		return Sensors.orient;
	}

	public static void startListenerDeviceOrientation(Context c) throws Exception {
		SensorManager sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);

		List<android.hardware.Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensorList.size() <= 0) {
		  	throw new Exception("ACCELEROMETER sensor not present");
		}

		if(!isListenerEnable){
		    sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
		    isListenerEnable = true;
		}

	}
	
}
