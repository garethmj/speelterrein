package be.ib.fit;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;


public class FitToGPXTest {


	@Test
	public void testWithFile() {
		FitToGPX.convertFitToGpx("test/data/2011-05-27-20-32-10.fit","test/data/converted.gpx");
		
		// and verify that file was created
		File convertedFile = new File("converted.gpx");
		Assert.assertTrue(convertedFile.exists());
	}
	
	@Test
	public void testWithDirectory() {
		FitToGPX.convertFitToGpx("test/data/","test/data/output");
	}
	
}
