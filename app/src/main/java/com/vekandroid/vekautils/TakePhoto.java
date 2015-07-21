package com.vekandroid.vekautils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import vekaUtils.IO;
import vekaUtils.Image;
import vekaUtils.Sensors;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class TakePhoto extends Activity  implements SurfaceHolder.Callback {

	protected Camera camera;
	protected SurfaceView surfaceCamera;
	protected PackageManager pm ;
	protected int currentCameraId = -1;

	private String file, tempFile ;
	protected boolean statusPhoto = false;
	protected boolean photoEnCours = false;

	protected boolean flash = false;
	protected boolean autoFocus = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		
		Bundle extras = getIntent().getExtras();
		file = extras.getString("output");
		tempFile = file+".temp";

		
		try {
			Sensors.startListenerDeviceOrientation(this);
		} catch (Exception e) {
			// no accelerometer
		}
		
		pm = this.getPackageManager();

		initSurfaceView();
		
		// hide camera switch if the device has only one 
		if (Camera.getNumberOfCameras() == 1)
			((ImageButton) findViewById(R.id.switchcamera)).setVisibility(View.VISIBLE);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		startPreview();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopCamera();
	}

	@Override
	public void onResume() {
		super.onResume();

		int cameraId;
		
		if(currentCameraId != -1)
			cameraId = currentCameraId;
		else
			cameraId = getSwitchcCameraId();
		
		initializeCamera(cameraId);

		showInterface1();
		
	}

	@Override
	public void onPause() {
		super.onPause();
		stopCamera();
	}

	@Override
	protected void onStop() {		
		super.onStop();

		findViewById(R.id.reset).setVisibility(View.GONE);

		// supprime le fichier temporaire
		IO.deleteFile(tempFile);
		
		stopCamera();
	}


	public boolean isFlash() {
		return flash;
	}

	/**
	 * Enable or Disable the flash
	 * @param flash
	 */
	public void setFlash(boolean flash) {

		
		Camera.Parameters parameters = camera.getParameters();
        if (parameters.getFlashMode() == null) {
			((ImageButton) findViewById(R.id.switchFlash)).setVisibility(View.GONE);
            return ;
        }		
		
		List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null || flashModes.isEmpty() || flashModes.size() == 1 && flashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
			((ImageButton) findViewById(R.id.switchFlash)).setVisibility(View.GONE);
            return ;
        }		
		
		
		
		// si pas de flash ou si camera frontal
		if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) || (currentCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT)) {
			
			((ImageButton) findViewById(R.id.switchFlash)).setVisibility(View.GONE);
			return ;
		}

		ImageButton button = (ImageButton) findViewById(R.id.switchFlash);
		button.setVisibility(View.VISIBLE);
				
		this.flash = flash;
		flash();
		
		if(isFlash())
			button.setImageResource(R.drawable.flash);
		else
			button.setImageResource(R.drawable.noflash);			
	}

	public boolean isAutoFocus() {
		return autoFocus;
	}

	/**
	 * set the autoFocus
	 * @param autoFocus
	 */
	public void setAutoFocus(boolean autoFocus) {
		this.autoFocus = autoFocus;
		autoFocus();
	}

	/**
	 * Prepare la SurfaceView
	 */
	private void initSurfaceView() {
		// Preparation de la SurfaceView
		surfaceCamera = (SurfaceView) findViewById(R.id.surfaceViewCamera);
		surfaceCamera.getHolder().addCallback(this);
	}

	/**
	 * initialise la camera
	 */
	private void initializeCamera(int cameraId) {

		// si aucune camera
		if(!isExistCamera())
			return ; 

		// si aucune camera
		if(camera != null)
			return ; 
		
		// get camera id
		currentCameraId = cameraId;

		// open camera
		try {
			camera = Camera.open(currentCameraId);
		} catch (Exception e) {
			Log.e("debug","echec : "+e.getMessage());
		}
		
		camera.setDisplayOrientation(getRotation());

		setAutoFocus(isAutoFocus());
		setFlash(isFlash());

	}
	
	/**
	 * set camera to continually auto-focus 
	 */
	private void autoFocus() {
		
		if(!isAutoFocus())
			return ;

		Camera.Parameters parameters = camera.getParameters();
		List<String> focusModes = parameters.getSupportedFocusModes();
		if (!focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
			return ;
		
		Camera.Parameters params = camera.getParameters();
		params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		camera.setParameters(params);
	}

	/**
	 * check if the device has any camera
	 * @return
	 */
	private boolean isExistCamera() {
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);		
	}
	
	/**
	 * stop the preview of Camera
	 */
	private void stopCamera() {		
		
		if (camera == null)
			return ;

		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
	private int getRotation() {
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
	    int degrees = 0;
	    
	    switch (rotation)
	    {
		    case Surface.ROTATION_0:
		        degrees = 90;
		        break;
		    case Surface.ROTATION_90:
		        degrees = 0;
		        break;
		    case Surface.ROTATION_180:
		        degrees = 270;
		        break;
		    case Surface.ROTATION_270:
		        degrees = 180;
		        break;
	    }
	    return degrees;
	}
	
	/**
	 * Switch camera ( back / front )
	 * @param V
	 */
	public void switchcamera(View V) {

		int i = getSwitchcCameraId();
		
		stopCamera();
				
		initializeCamera(i);
		
		startPreview();
	
	}

	/**
	 * attache la SurfaceView à la camera et demarre la preview
	 */
	private void startPreview() {
		// attache la camera a la surfaceView
		try {
			camera.setPreviewDisplay(surfaceCamera.getHolder());
			camera.startPreview();
		} catch (IOException e) {
			Log.e("debug", "echec : "+e.getMessage());
		}
		
	}

	/**
	 * get Id of next camera ( default front)
	 * @return
	 */
	private int  getSwitchcCameraId() {

		int i;

		switch (currentCameraId) {
		case Camera.CameraInfo.CAMERA_FACING_FRONT:
			i = Camera.CameraInfo.CAMERA_FACING_BACK;
			break;

		case Camera.CameraInfo.CAMERA_FACING_BACK:
		default:

			if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
				i = Camera.CameraInfo.CAMERA_FACING_FRONT;
			else
				i = Camera.CameraInfo.CAMERA_FACING_BACK;
						
			break;
		}

		return i;
		
	}
	
	public void switchFlash(View v) {
		setFlash(!isFlash());
		
		stopCamera();
				
		initializeCamera(currentCameraId);
		
		startPreview();
	
	
	}
	
	/**
	 * set camera to Flash
	 */
	private void flash() {
		
		if(!isFlash())
			return ;
		
		Camera.Parameters parameters = camera.getParameters();


        if (parameters.getFlashMode() == null) {
            return ;
        }		
		
		List<String> flashModes = parameters.getSupportedFlashModes();

        if (flashModes == null || flashModes.isEmpty() || flashModes.size() == 1 && flashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return ;
        }		
		
		String strFlash = android.hardware.Camera.Parameters.FLASH_MODE_ON;
				
		if (flashModes.contains(strFlash))
		     parameters.setFlashMode(Parameters.FLASH_MODE_ON);

		camera.setParameters(parameters);		
		
	}

	
	
	public void takePhoto(View v){
		statusPhoto = true;
		photoEnCours = true;

		// cacher les boutons et afficher un message

		findViewById(R.id.cancel).setVisibility(View.GONE);		
		findViewById(R.id.ok).setVisibility(View.GONE);
		findViewById(R.id.message).setVisibility(View.VISIBLE);
		
		findViewById(R.id.switchFlash).setVisibility(View.GONE);
		findViewById(R.id.switchcamera).setVisibility(View.GONE);

		camera.takePicture(null, null, jpegCallback);
	}

	
	
	
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {

			
		    Bitmap storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);

		    // tourne la photo suivant l'orientation device
		    try {
			    Matrix mat = new Matrix();
			    int rotate = getRotationForSave();
			    			    
				mat.postRotate(rotate);
			    storedBitmap = Bitmap.createBitmap(storedBitmap, 0, 0, storedBitmap.getWidth(), storedBitmap.getHeight(), mat, true);
			} catch (Exception e) {
				// pas d'accellerometre pour verifier l'orientation
			}

		    // convertie le bitmap en en tableau de byte en PNG
		    data = Image.bitmapToByteArrayOfJPG(storedBitmap);
			
			// enregistre la photo 
			if(!IO.saveBytesToFile(data, new File(tempFile)))
			{				

				findViewById(R.id.cancel).setVisibility(View.VISIBLE);		
				findViewById(R.id.ok).setVisibility(View.VISIBLE);
				findViewById(R.id.message).setVisibility(View.GONE);
				findViewById(R.id.validate).setVisibility(View.GONE);

				if((currentCameraId != Camera.CameraInfo.CAMERA_FACING_FRONT))
					findViewById(R.id.switchFlash).setVisibility(View.VISIBLE);

				Camera.Parameters parameters = camera.getParameters();
		        if (parameters.getFlashMode() == null) {
					findViewById(R.id.switchFlash).setVisibility(View.GONE);
		        }		
				
				List<String> flashModes = parameters.getSupportedFlashModes();
		        if (flashModes == null || flashModes.isEmpty() || flashModes.size() == 1 && flashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
					findViewById(R.id.switchFlash).setVisibility(View.GONE);
		        }		
				
				if (Camera.getNumberOfCameras() > 1)
					findViewById(R.id.switchcamera).setVisibility(View.VISIBLE);
							
				
				Toast.makeText(getApplicationContext(), R.string.errorFailWriteFile, Toast.LENGTH_LONG).show();
				statusPhoto = false;
				return ;
			}

			findViewById(R.id.message).setVisibility(View.GONE);
			findViewById(R.id.validate).setVisibility(View.VISIBLE);
			findViewById(R.id.reset).setVisibility(View.VISIBLE);
			findViewById(R.id.cancel).setVisibility(View.VISIBLE);		
			
			photoEnCours = false;
		}

		/**
		 * renvoi l'orientation du device ( en degrées)
		 * @return
		 * @throws Exception
		 */
		private int getRotationForSave() throws Exception {	
			
			int rotation = Sensors.getOrientation(TakePhoto.this);
						
			// impossible car orientation fix
//			int rotation = getWindowManager().getDefaultDisplay().getRotation();
	
			int degrees = 0;
		    
		    switch (rotation)
		    {
			    case Surface.ROTATION_0:
			        degrees = 270;
			        break;
			    case Surface.ROTATION_90:
			        degrees = 180;
			        break;
			    case Surface.ROTATION_180:
			        degrees = 90;
			        break;
			    case Surface.ROTATION_270:
			        degrees = 0;
			        break;
		    }
		    
			return degrees;
		}
	};
	
	public void cancelPhoto(View v) {
		
		finish();
		
	}
	
	public void validate(View v) {
		
		IO.moveFile(tempFile, file);
		IO.deleteFile(tempFile);

		Intent returnIntent = new Intent();
		returnIntent.putExtra("output",file);
		setResult(RESULT_OK,returnIntent);
		
		finish();
	}
	
	public void reset(View v) {
		
		if(photoEnCours)
			return ;
		
		camera.startPreview();

		v.setVisibility(View.GONE);

		if(!IO.deleteFile(tempFile))
			Toast.makeText(this, R.string.errorFileNotFound, Toast.LENGTH_LONG).show();
		else
			statusPhoto = false;

		showInterface1();
	}
	
	private void showInterface1() {
		findViewById(R.id.cancel).setVisibility(View.VISIBLE);		
		findViewById(R.id.ok).setVisibility(View.VISIBLE);
		findViewById(R.id.message).setVisibility(View.GONE);
		findViewById(R.id.validate).setVisibility(View.GONE);
			
		if((currentCameraId != Camera.CameraInfo.CAMERA_FACING_FRONT))
			findViewById(R.id.switchFlash).setVisibility(View.VISIBLE);

		if (Camera.getNumberOfCameras() > 1)
			findViewById(R.id.switchcamera).setVisibility(View.VISIBLE);

	}
	
	
}
