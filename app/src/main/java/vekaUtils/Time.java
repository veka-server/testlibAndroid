package vekaUtils;

public class Time {
	
	/**
	 * convert a number of second to a humain readable String
	 * @param totalSecs long
	 * @return String format like HH:MM:SS
	 */
	public static String convertHumainReadable(long totalSecs){
		long hours = totalSecs / 3600;
		long minutes = (totalSecs % 3600) / 60;
		long seconds = totalSecs % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

}
