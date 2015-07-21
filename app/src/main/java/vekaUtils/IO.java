package vekaUtils;

import java.io.File;
import java.io.FileOutputStream;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;


public class IO {

	/**
	 * get path to folder sdCard
	 * @return String 
	 */
	public static String getPathToSdCardFolder() {
		return Environment.getExternalStorageDirectory().getPath()+"/Android/data/";
	}

	/**
	 * Save an array of byte 
	 * @param bytes
	 * @param file 
	 * @return true if save is ok
	 */
	public static boolean saveBytesToFile(byte[] bytes, File file){
		
    	// créer les dossier si il n'existe pas
    	file.getParentFile().mkdirs();
    	
    	// tente d'ecrire le fichier
		try {
	    	FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {} 
		
		return false;

	}
	
	/**
	 * delete a file if exist
	 * @param uri
	 * @return
	 */
	public static boolean deleteFile(Uri uri) {
		return deleteFile(new File(uri.getPath()));
	}
	
	/**
	 * delete a file if exist
	 * @param chemin
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String chemin, String fileName) {
		return deleteFile(chemin+fileName);
	}
	
	/**
	 * delete a file if exist
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return deleteFile(file);
	}

	/**
	 * delete a file if exist
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
		if(file.exists()) 
			return file.delete();
		return false;
	}

	/**
	 * move a file if exist
	 * @param file File source
	 * @param file File destination
	 * @return
	 */
	public static boolean moveFile(File source, File destination) {
		if(source.exists()) 
			return source.renameTo(destination);
		return false;
	}

	/**
	 * move a file if exist
	 * @param file String source
	 * @param file String destination
	 * @return
	 */
	public static boolean moveFile(String source, String destination) {
		File s = new File(source);
		File d = new File(destination);
		return moveFile(s, d);		
	}

}
