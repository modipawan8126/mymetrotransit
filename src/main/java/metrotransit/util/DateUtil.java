package metrotransit.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	/**
	 * @param timeStamp
	 * @return
	 * 
	 * 		This method to calculate minutes difference between next bus
	 *         departure time to current system time.
	 */
	public static Long getTimeToNextBus1(String timeStamp) {

		long milliseconds1 = Long.parseLong(getEpochTimeStamp(timeStamp));
		long milliseconds2 = System.currentTimeMillis();

		long diff = milliseconds1 - milliseconds2;
		// long diffSeconds = diff / 1000;
		long diffMinutes = diff / (60 * 1000);
		// long diffHours = diff / (60 * 60 * 1000);
		// long diffDays = diff / (24 * 60 * 60 * 1000);

		// System.out.println(diffMinutes);
		return diffMinutes;
	}
	
	public static String getCurrentDateTime(String timezone, String dateFormat) {
		Date date = new Date();
        DateFormat format = new SimpleDateFormat(dateFormat);
        format.setTimeZone(TimeZone.getTimeZone(timezone));
        String formatted = format.format(date);
        
        return formatted;
	}

	private static String getEpochTimeStamp(String s) {
		String timeStamp = s.substring(6, 19);
		return timeStamp;
	}

	public static Date getDateForGivenTimeStamp(String timeStamp) {
		Long timeStampL = Long.valueOf(timeStamp);
		Date date = new Date(timeStampL);
		return date;
	}
}
