package ir.khalili.products.odds.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * @author avihang
 *
 */
public class CalenderUtil {

	private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private static DateFormat dfShort = DateFormat.getDateInstance(DateFormat.SHORT, new ULocale("fa_IR@calendar=persian"));
	private static Calendar calendar = Calendar.getInstance(new ULocale("fa_IR@calendar=persian"));
	
	public static int getPersianYearMonth() {
		
		String[] persianDate = dfShort
				.format(calendar).
				replaceAll("[۰]", "0").
				replaceAll("[۱]", "1").
				replaceAll("[۲]", "2").
				replaceAll("[۳]", "3").
				replaceAll("[۴]", "4").
				replaceAll("[۵]", "5").
				replaceAll("[۶]", "6").
				replaceAll("[۷]", "7").
				replaceAll("[۸]", "8").
				replaceAll("[۹]", "9").
				split("/");
		
		String result = persianDate[0]+(persianDate[1].length()==1?("0"+persianDate[1]):persianDate[1]);
		
		return Integer.parseInt(result);
	}
	
	public static String getPersianDate() {
		
		String[] persianDate = dfShort
				.format(calendar).
				replaceAll("[۰]", "0").
				replaceAll("[۱]", "1").
				replaceAll("[۲]", "2").
				replaceAll("[۳]", "3").
				replaceAll("[۴]", "4").
				replaceAll("[۵]", "5").
				replaceAll("[۶]", "6").
				replaceAll("[۷]", "7").
				replaceAll("[۸]", "8").
				replaceAll("[۹]", "9").
				split("/");
		
		return persianDate[0]+(persianDate[1].length()==1?("0"+persianDate[1]):persianDate[1])+(persianDate[2].length()==1?("0"+persianDate[2]):persianDate[2]);
	}
	
	public static String getPersianDateTime() {
		
		String persianDate = dfShort
				.format(calendar).
				replaceAll("[۰]", "0").
				replaceAll("[۱]", "1").
				replaceAll("[۲]", "2").
				replaceAll("[۳]", "3").
				replaceAll("[۴]", "4").
				replaceAll("[۵]", "5").
				replaceAll("[۶]", "6").
				replaceAll("[۷]", "7").
				replaceAll("[۸]", "8").
				replaceAll("[۹]", "9");
		
		return persianDate + " " + sdfTime.format(new Date());
	}
	
	public static String getPersianYear() {
		return getPersianDate().substring(2, 4);
	}
	
	public static void main(String[] args) {
		
//		System.out.println(getPersianDateTime());
		final int DAY_OF_WEEK = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		final int HOUR_OF_DAY = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		System.out.println("DAY_OF_WEEK:"+DAY_OF_WEEK);
		System.out.println("HOUR_OF_DAY:"+HOUR_OF_DAY);
	}
	
}
