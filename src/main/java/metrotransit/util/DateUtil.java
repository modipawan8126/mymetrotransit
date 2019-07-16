package metrotransit.util;

import java.util.Date;

public class DateUtil {

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
