/*
 * Copyright 2003-2009 LCM-ANMC, Inc. All rights reserved.
 * This source code is the property of LCM-ANMC, Direction
 * Informatique and cannot be copied or distributed without
 * the formal permission of LCM-ANMC.
 */
package be.cm.comps.cache.test.util;

import java.io.PrintStream;

/**
 * Helper class to measure performance using nanoseconds precision.
 * 
 * @author 7515005 Ivan Belis
 * 
 */
public class NanoMeter {

	// the number of nano to miliseconds, this is a double because we will use
	// it in calculation with results after the decimal sign
	//
	private static final double NANO_TO_MILISEC_RATIO = 1000000.0;

	// @formatter:off
	private PrintStream writer = System.out; // the stream to write messages to
												// (default stdout)
	private long startTime = System.nanoTime(); // the timestamp when this class
												// was created
	private long previousLogTime = startTime; // the timestamp of the last log
												// smessage
	// @formatter:on

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

		writer.printf("%,7.2f ms | %,7.2f ms |", ((double) elapsedTime) / NANO_TO_MILISEC_RATIO,
				((double) totalElapsedTime) / NANO_TO_MILISEC_RATIO);
		writer.println(logmessage);

		previousLogTime = System.nanoTime();
	}

}
