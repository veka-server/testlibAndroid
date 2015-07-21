package vekaUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

public class Network {

	/**
	 * Get the number of data (Mobile, 3G, 4G, Edge, ...) used since the device boot
	 * @return long
	 */
	public static long getMobileCurrentData() {
		// get current values of counters
		long currentMobileTxBytes = TrafficStats.getMobileTxBytes();
		long currentMobileRxBytes = TrafficStats.getMobileRxBytes();
	
		// to get mobile data count, subtract old from current
		final long currentMobileSent = currentMobileTxBytes - 0;
		final long currentMobileReceived = currentMobileRxBytes - 0;

    	return currentMobileReceived + currentMobileSent;
	}
	
	/**
	 * Get the number of data (wifi) used since the device boot
	 * @return long
	 */
	public static long getWifiCurrentData() {
		// get current values of counters
		long currentMobileTxBytes = TrafficStats.getMobileTxBytes();
		long currentMobileRxBytes = TrafficStats.getMobileRxBytes();
		long totalTxBytes = TrafficStats.getTotalTxBytes();
		long totalRxBytes = TrafficStats.getTotalRxBytes();			

		// to get WiFi/LAN data count, subtract total from mobile
		final long currentNetworkSent = totalTxBytes - currentMobileTxBytes;
		final long currentNetworkReceived = totalRxBytes - currentMobileRxBytes;	
	
    	return currentNetworkReceived + currentNetworkSent;
	}

	/**
	 * Return a String with an humain readable number of octet
	 * @param number
	 * @return
	 */
	public static String bytesToHumainReadableOctet(double number) {

		String str = "";
		double ko = 1024L;
		double mo = ko*ko;
		double go = ko*ko*ko;
		
		if(number >= go) {
			str = (double)Math.round((number/go)*(double)100)/(double)100+" Go";
		}
		else if(number >= mo) {
			str = (double)Math.round((number/mo)*(double)100)/(double)100+" Mo";
		}
		else if(number >= ko) {
			str = (double)Math.round((number/ko)*(double)100)/(double)100+" Ko";
		}
		else {
			str = number+" o";
		}
		
		return str;
	}
	


	public static boolean isOnline(Context c) {
	    ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
	}

}
