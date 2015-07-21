package vekaUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import exception.IntentException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore;

public class Image {	
	
	public static final String FORMAT_PNG = "PNG";
	public static final String FORMAT_JPEG = "JPEG";
	public static final String FORMAT_WEBP = "WEBP";
	
	
	/**
	 * Get intent to call activity for take a photo with official google app
	 * @param pm PackageManager
	 * @param imageUri Uri for output file
	 * @return intent
	 * @throws IntentException if intent is not reachable (official google app removed)
	 * 
	 * <h3>WARNING</h3>
	 * many bug with this official intent.
	 * 
	 * @deprecated use intentTakePhoto(Context c, String outputFilePath )
	 * 
	 */
	public static Intent intentTakePhoto(PackageManager pm, Uri imageUri ) throws IntentException {

		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); 
		
		if (!Divers.isIntentReachable(pm, i))
			throw new IntentException();
		return i;
				
	}


    /**
     * Get intent to call an activity for take a photo
     * @param c context 
     * @param outputFilePath path for output file
     * @return intent
     */
    public static Intent intentTakePhoto(Context c, String outputFilePath ) {

		Intent i = new Intent(c,com.vekandroid.vekautils.TakePhoto.class);
		i.putExtra("output", outputFilePath); 
		return i;
    }
    
	
	
	/**
	 * Get intent to call activity for select an image from official google gallery
	 * @param pm PackageManager
	 * @return intent
	 * @throws IntentException if intent is not reachable (official google gallery removed)
	 */
	public static Intent intentGetImage(PackageManager pm ) throws IntentException {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);				 
		if (!Divers.isIntentReachable(pm, i))
			throw new IntentException();
		return i;
	}
	
	/**
	 * Get intent to call activity for crop an image from official google app
	 * @param pm PackageManager
	 * @param source Uri of source image file
	 * @param destination Uri of destination file
	 * @param aspectX X ratio
	 * @param aspectY Y ratio
	 * @return intent
	 * @throws IntentException if intent is not reachable (official google app removed)
	 * @deprecated 
	 */
	public static Intent intentCropImage(PackageManager pm, Uri source, Uri destination, int aspectX, int aspectY) throws IntentException {

        Intent intent = new Intent("com.android.camera.action.CROP");
	     intent.setDataAndType(source, "image/*");
	     intent.putExtra("crop", "true");
	     // this defines the aspect ration
	     intent.putExtra("aspectX", aspectX);
	     intent.putExtra("aspectY", aspectY);	     
	     // true to return a Bitmap, false to directly save the cropped image
	     intent.putExtra("return-data", false);
	     intent.putExtra("output", destination);
	     intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG); 

		if (!Divers.isIntentReachable(pm, intent))
			throw new IntentException();
		return intent;
	}
	
	/**
	 * Get intent to call activity for crop an image.<br/>
	 * The new image will be saved on the destination path.
	 * 
	 * @param context Context
	 * @param source String of source image file
	 * @param destination String of destination file
	 * @param outputX int width of image dest
	 * @param outputY int height of image dest
	 * @param outputFormat String format of finale file <i>( Image.FORMAT_PNG, Image.FORMAT_JPEG, ... )</i>
	 * @return intent
	 */
	public static Intent intentCropImage(Context context, String source, String destination, int outputX, int outputY, String outputFormat)  {
		
		Intent intent = new Intent(context, cropImage.CropImage.class);

		// image source
		intent.putExtra("src-image-path", source);
		intent.putExtra("dest-image-path", destination);
		
		// RATIO 
		intent.putExtra("aspectY", outputY);
		intent.putExtra("aspectX", outputX);
		
		// Taille de l'image en sortie 
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputX", outputX);
		
		// Taille minimum pour la crop zone
		intent.putExtra("minSizeY", outputY);
		intent.putExtra("minSizeX", outputX);
		
		// ???
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);

		// FORMAT DE L'IMAGE
		intent.putExtra("outputFormat", "PNG");
		
		// Crop circle
//		intent.putExtra("circleCrop", 1);

		return intent;
	}

	
	/**
	 * Get intent to call activity for crop an image.<br/>
	 * The new Bitmap will be return on intent with name "data".
	 * <br/><br/>
	 * 	Bitmap img = data.getParcelableExtra("data");
	 * <br/><br/>
	 * @param context Context
	 * @param source String of source image file
	 * @param outputX width of image dest
	 * @param outputY height of image dest
	 * @return intent
	 */
	public static Intent intentCropImageForResult(Context context, String source, int outputX, int outputY )  {
		
		Intent intent = new Intent(context, cropImage.CropImage.class);

		// image source
		intent.putExtra("src-image-path", source);
		
		// RATIO 
		intent.putExtra("aspectY", outputY);
		intent.putExtra("aspectX", outputX);
		
		// Taille de l'image en sortie 
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputX", outputX);
		
		// Taille minimum pour la crop zone
		intent.putExtra("minSizeY", outputY);
		intent.putExtra("minSizeX", outputX);
		
		// autorise le redimenssionement de l'image si elle est trops grande pour le format d'enregistrement
		intent.putExtra("scale", true);

		// autorise le redimenssionement de l'image si elle n'est pas assez grande pour le format d'enregistrement
		intent.putExtra("scaleUpIfNeeded", true);

		// FORMAT DE L'IMAGE
//		intent.putExtra("outputFormat", outputFormat);
		
		// Crop circle
//		intent.putExtra("circleCrop", 1);

		return intent;
	}

	
	
	/**
	 * Get intent to call activity for crop an image from official google app
	 * @param pm PackageManager
	 * @param source String of source image file
	 * @param destination String of destination file
	 * @param aspectX X ratio
	 * @param aspectY Y ratio
	 * @return intent
	 * @throws IntentException if intent is not reachable (official google app removed)
	 * @deprecated 
	 */
	public static Intent intentCropImage(PackageManager pm, String source, String destination, int aspectX, int aspectY) throws IntentException {

		return Image.intentCropImage(pm, new File(source), new File(destination), aspectX, aspectY);
	}
	
	/**
	 * Get intent to call activity for crop an image from official google app
	 * @param pm PackageManager
	 * @param source File of source image file
	 * @param destination File of destination file
	 * @param aspectX X ratio
	 * @param aspectY Y ratio
	 * @return intent
	 * @throws IntentException if intent is not reachable (official google app removed)
	 * @deprecated 
	 */
	public static Intent intentCropImage(PackageManager pm, File source, File destination, int aspectX, int aspectY) throws IntentException {

		return Image.intentCropImage(pm, Uri.fromFile(source), Uri.fromFile(destination), aspectX, aspectY);
	}
	
	/**
	 * convert an image bitmap to an array of byte compress with png
	 * @param b Bitmap image
	 * @return array of byte (PNG)
	 */
	public static byte[] bitmapToByteArrayOfPNG(Bitmap b) {
    	int pixels = b.getHeight() * b.getRowBytes();
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(pixels);
    	b.compress(CompressFormat.PNG, 0, baos);
    	
		return baos.toByteArray();
	}

	
	/**
	 * convert an image bitmap to an array of byte compress with jpg
	 * @param b Bitmap image
	 * @return array of byte (JPG)
	 */
	public static byte[] bitmapToByteArrayOfJPG(Bitmap b) {
    	int pixels = b.getHeight() * b.getRowBytes();
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(pixels);
    	b.compress(CompressFormat.JPEG, 100, baos);
    	
		return baos.toByteArray();
	}

	/**
	 * Save an bitmap image (PNG)
	 * @param b bitmap 
	 * @param file
	 * @return
	 */
	public static boolean saveBitmapToPNG(Bitmap b, File file) {
		return IO.saveBytesToFile(bitmapToByteArrayOfPNG(b), file);
	}

	/**
	 * Save an bitmap image (JPG)
	 * @param b bitmap 
	 * @param file
	 * @return
	 */
	public static boolean saveBitmapToJPG(Bitmap b, File file) {
		return IO.saveBytesToFile(bitmapToByteArrayOfJPG(b), file);
	}

	/**
	 * decode a bitmap image with sample
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromStringPath(String path, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path,options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path,options);
	}

	/**
	 * decode a bitmap image with sample
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;

	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

		return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/**
	 * Calculate the samplesize
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}


	
}
