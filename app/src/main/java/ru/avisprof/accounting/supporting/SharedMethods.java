package ru.avisprof.accounting.supporting;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;
import android.widget.Toast;

/**Вспомогательные методы
 * Created by Leonid on 22.04.2016.
 */
public class SharedMethods {
	private static final String TAG = "SharedMethods";

	public static String getSumString(double sum) {
		//return String.format("%.2f", sum);
		return new DecimalFormat("##,##0.00").format(sum).replace(",",".");
	}

	public static String getDateString(Date date) {
		
		if (date == null) {
			return "";
		}
		
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		return String.format("%02d.%02d.%04d", day, month+1, year);
		
	}
	
	public static String getDateHeaderString(Date date) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, EEEE");
		return sdf.format(calendar.getTime()).toLowerCase();
	}

	public static boolean isSameDate(Date date1, Date date2) {
		
		if (date1 == null) {
			return false;
		}
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		
		if (date2 == null) {
			return false;
		}
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		
		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
			return false;
		}
		
		if (cal1.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR)) {
			return false;
		}
		
		return true;
	}

	public static long getStartOfDay(long date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		
		Calendar result = Calendar.getInstance();
		result.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		result.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));
		return result.getTimeInMillis();
	}
	
	

}
