package vekaUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public abstract class DownloadImageTask extends AsyncTask<Object, Void, Bitmap> {
	
    //after downloading
    protected abstract void onPostExecute(Bitmap result);
    
	@Override
	protected Bitmap doInBackground(Object... params) {
        String urldisplay = (String) params[0];
        int w = (Integer) params[1];
        int h = (Integer) params[2];
        Bitmap mIcon11 = null;
        InputStream in;
		try {
			in = new java.net.URL(urldisplay).openStream();
		      mIcon11 = decodeSampledBitmapFromStream(in,w,h);
		} catch (MalformedURLException e) {
			MalformedURLException();
		} catch (IOException e) {
			IOException();
		}
        return mIcon11;
	}
	
	public void MalformedURLException(){
		
	}
	
	public void IOException(){
		
	}

	
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

	public static Bitmap decodeSampledBitmapFromFile(String link, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(link, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(link, options);
	}
	
	public static byte[] toByteArray(InputStream is) throws IOException{
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[16384];

				while ((nRead = is.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
				}
				buffer.flush();

				return buffer.toByteArray();
	}
		
	public static Bitmap decodeSampledBitmapFromStream(InputStream in, int reqWidth, int reqHeight) {

		InputStream is;
		byte[] bytes;

		try {
			bytes = toByteArray(in);
		} catch (IOException e) {
			return null;
		}
		        
		// First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}
	
	
  }