////////////////////////////////////////////////////////////////////////////////
// The following FIT Protocol software provided may be used with FIT protocol
// devices only and remains the copyrighted property of Dynastream Innovations Inc.
// The software is being provided on an "as-is" basis and as an accommodation,
// and therefore all warranties, representations, or guarantees of any kind
// (whether express, implied or statutory) including, without limitation,
// warranties of merchantability, non-infringement, or fitness for a particular
// purpose, are specifically disclaimed.
//
// Copyright 2008 Dynastream Innovations Inc.
////////////////////////////////////////////////////////////////////////////////
// ****WARNING****  This file is auto-generated!  Do NOT edit this file.
// Profile Version = 1.20Release
// Tag = $Name: AKW1_200 $
////////////////////////////////////////////////////////////////////////////////


package com.garmin.fit.csv;

import com.garmin.fit.*;

public class SessionCSVWriter implements SessionMesgListener {
   private CSVWriter csv;

   public SessionCSVWriter(String fileName) {
      csv = new CSVWriter(fileName);
   }

   public void close() {
      csv.close();
   }

   public void onMesg(SessionMesg mesg) {
      if (mesg.getTimestamp() != null)
         csv.set("Timestamp", mesg.getTimestamp().toString());
      if (mesg.getMessageIndex() != null)
         csv.set("Session #", Integer.toString(mesg.getMessageIndex() + 1));
      if (mesg.getFirstLapIndex() != null)
         csv.set("First Lap #", Integer.toString(mesg.getFirstLapIndex() + 1));
      if (mesg.getNumLaps() != null)
         csv.set("Laps", mesg.getNumLaps().toString());
      if (mesg.getSport() != null)
         csv.set("Sport", mesg.getSport().toString());
      if (mesg.getSubSport() != null)
         csv.set("Sub Sport", mesg.getSubSport().toString());
      if (mesg.getStartTime() != null)
         csv.set("Start Time", mesg.getStartTime().toString());
      if (mesg.getTotalTimerTime() != null)
         csv.set("Total Timer Time [s]", mesg.getTotalTimerTime().toString());
      if (mesg.getTotalElapsedTime() != null)
         csv.set("Total Elapsed Time [s]", mesg.getTotalElapsedTime().toString());
      if (mesg.getStartPositionLat() != null)
         csv.set("Start Latitude [semicircles]", mesg.getStartPositionLat().toString());
      if (mesg.getStartPositionLong() != null)
         csv.set("Start Longitude [semicircles]", mesg.getStartPositionLong().toString());
      if (mesg.getTotalDistance() != null)
         csv.set("Total Distance [m]", mesg.getTotalDistance().toString());
      if (mesg.getTotalCycles() != null)
         csv.set("Total Cycles [cycles]", mesg.getTotalCycles().toString());
      if (mesg.getTotalCalories() != null)
         csv.set("Total Calories [calories]", mesg.getTotalCalories().toString());
      if (mesg.getTotalAscent() != null)
         csv.set("Total Ascent [m]", mesg.getTotalAscent().toString());
      if (mesg.getTotalDescent() != null)
         csv.set("Total Descent [m]", mesg.getTotalDescent().toString());
      if (mesg.getAvgSpeed() != null)
         csv.set("Avg Speed [m/s]", mesg.getAvgSpeed().toString());
      if (mesg.getMaxSpeed() != null)
         csv.set("Max Speed [m/s]", mesg.getMaxSpeed().toString());
      if (mesg.getAvgHeartRate() != null)
         csv.set("Avg Heart Rate [bpm]", mesg.getAvgHeartRate().toString());
      if (mesg.getMaxHeartRate() != null)
         csv.set("Max Heart Rate [bpm]", mesg.getMaxHeartRate().toString());
      if (mesg.getAvgCadence() != null)
         csv.set("Avg Cadence [rpm]", mesg.getAvgCadence().toString());
      if (mesg.getMaxCadence() != null)
         csv.set("Max Cadence [rpm]", mesg.getMaxCadence().toString());
      if (mesg.getAvgPower() != null)
         csv.set("Avg Power [watts]", mesg.getAvgPower().toString());
      if (mesg.getMaxPower() != null)
         csv.set("Max Power [watts]", mesg.getMaxPower().toString());
      csv.writeln();
   }
}
