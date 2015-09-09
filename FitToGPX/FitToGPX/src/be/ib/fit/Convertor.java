package be.ib.fit;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import com.garmin.fit.DateTime;
import com.garmin.fit.Field;

/**
 * Conversion utilities for translating the garmin coördinates to the GPX
 * coördinates.
 * 
 * @author Ivan Belis
 * 
 */
public class Convertor {
	
	private static final BigDecimal MULTIPLICANT = new BigDecimal(180.0d /  Math.pow(2L , 31L));

	/*
	 * Convert a fit field coördinate to a GPX coördinate.
	 * It will only convert fit fields with unit "semicircles".
	 * 
	 * dms=semicircles*(180/2^31)
	 */
	public static BigDecimal semicircleToDms(Field field) {
		BigDecimal dms = null;
		if ((field != null) && ("semicircles".equals(field.getUnits()))) {
			dms = semicircleToDms(field.getLongValue());
		}
		return dms; // 42.059009616
	}
	
	

	/**
	 * Convert a semicircles coördinate (lattitude or longitude) value to a degrees value
	 *  using the formula :
	 * dms=semicircles*(180/2^31)
	 * 
	 * s * (180.0D / 2^31)
	 * 
	 * 
	 * @see http://www.gps-forums.net/accuracy-converting-semicircles-degrees-t31488.html
	 */
	public static BigDecimal semicircleToDms(long semicircle) {
		BigDecimal dms = new BigDecimal(semicircle).multiply(MULTIPLICANT);
		return dms; 
	}
	
	/**
	 * Convert the fit time field to the gpx time format (standard UTC time).
	 * Will only convert "s" seconds.
	 * @param time
	 * @return
	 */
	public static String convertTime(Field time) {
		String result = null;
		if ((time != null)&&("s".equals(time.getUnits()))) {
			DateTime dateTime = new DateTime(time.getLongValue());
			result = convertTime(dateTime.getDate());
		}
		return result;
	}

	public static String convertTime(Date date) {
		TimeZone zone = TimeZone.getTimeZone("UTC");
		Calendar cal = Calendar.getInstance(zone);
		cal.setTime(date);
		return DatatypeConverter.printDateTime(cal);
	}


}
