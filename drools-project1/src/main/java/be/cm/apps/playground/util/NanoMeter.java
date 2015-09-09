package be.cm.apps.playground.util;

import java.io.PrintStream;

/**
 * Helper class to measure performance.
 * 
 * @author Ivan
 * 
 */
public class NanoMeter {

	// the number of nano to miliseconds, this is a double because we will use
	// it in calculation with results after the decimal sign
	//
	private static final double NANO_TO_MILISEC_RATIO = 1000000.0;

	//@formatter:off
	private PrintStream writer = System.out; 	// the stream to write messages to (default stdout)
	private long startTime = System.nanoTime(); // the timestamp when this class was created
	private long previousLogTime = startTime;   // the timestamp of the last log smessage
	
	private long freeMemoryStart;               // the available free memory
	//@formatter:onn

	public NanoMeter() {
		startTime = System.nanoTime();
		previousLogTime = startTime;
	}

	public NanoMeter(PrintStream printStream) {
		this();
		writer = printStream;
	}

	public void log(String logmessage) {
		// first take the timestamp, so that we do not measur timing from our
		// own routines
		long currentTime = System.nanoTime();
		long elapsedTime = currentTime - previousLogTime;
		long totalElapsedTime = currentTime - startTime;

		writer.printf("%,7.2f ms | %,7.2f ms |", ((double)elapsedTime) / NANO_TO_MILISEC_RATIO
				,((double)totalElapsedTime) / NANO_TO_MILISEC_RATIO);
		writer.println(logmessage);
		
		previousLogTime = System.nanoTime();
	}
	
	public void startMemory() {
		gc();  
        freeMemoryStart = Runtime.getRuntime().freeMemory();
//        this.log("Memory free: " + Runtime.getRuntime().freeMemory()
//    			+ " total:" + Runtime.getRuntime().totalMemory()); 
	}

	private void gc() {
		// hint the system to perform a gc()
        System.gc(); 
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// ignore here
		}
	}
	
	public void stopMemory() {
        gc();
        long memend = Runtime.getRuntime().freeMemory();
        this.log("Memory used: " + (freeMemoryStart - memend) 
        			+ "  from total:" + Runtime.getRuntime().totalMemory());
	}

}
