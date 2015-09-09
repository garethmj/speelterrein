package be.ib.fit;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.garmin.fit.DateTime;

public class ConvertorTest {

	@Test
	public void testSemicircleToDms1() {
		long semicircle = 609550339L; // lat in semicircle
		BigDecimal degrees = Convertor.semicircleToDms(semicircle);
		Assert.assertEquals("51.09191919", degrees.toPlainString().substring(0, 11));
	}

	@Test
	public void testSemicircleToDms2() {
		long semicircle = 52203101L; // long in semicircle
		BigDecimal degrees = Convertor.semicircleToDms(semicircle);
		Assert.assertEquals("4.375613378", degrees.toPlainString().substring(0, 11));
	}

	@Test
	public void testSemicircleToDms3() {
		long semicircle = 609843842; // lat in semicircle
		BigDecimal degrees = Convertor.semicircleToDms(semicircle);
		Assert.assertEquals("51.11652033", degrees.toPlainString().substring(0, 11));
	}
	
	@Test
	public void testConverTime() {
		long time = 675455530L;
		DateTime dateTime = new DateTime(time);
		
		Assert.assertEquals("2011-05-27T18:32:10Z", Convertor.convertTime(dateTime.getDate()));
		
		time = time + 1;  // increase a second
		dateTime = new DateTime(time);
		Assert.assertEquals("2011-05-27T18:32:11Z", Convertor.convertTime(dateTime.getDate()));
	}

}
