package vekaUtils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

public class Divers {

	/**
	 * Check if an intent is reachable
	 * @param pm PackageManager
	 * @param i intent
	 * @return boolean true = reachable
	 */
	public static boolean isIntentReachable(PackageManager pm, Intent i) {
		ActivityInfo activityInfo = i.resolveActivityInfo(pm, i.getFlags());
		return activityInfo.enabled;
	}
	
}
