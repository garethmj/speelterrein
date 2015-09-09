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


package com.garmin.fit;

public interface MesgWithEvent {
   public DateTime getTimestamp();
   public void setTimestamp(DateTime timestamp);
   public Event getEvent();
   public void setEvent(Event event);
   public EventType getEventType();
   public void setEventType(EventType type);
   public Short getEventGroup();
   public void setEventGroup(Short group);
}
