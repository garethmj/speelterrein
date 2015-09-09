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

public enum SessionTrigger {
   ACTIVITY_END((short)0),   
   MANUAL((short)1),   
   AUTO_MULTI_SPORT((short)2),   
   FITNESS_EQUIPMENT((short)3),   
   INVALID((short)255);


   protected short value;




   private SessionTrigger(short value) {
     this.value = value;
   }

   protected static SessionTrigger getByValue(final Short value) {
      for (final SessionTrigger type : SessionTrigger.values()) {
         if (value == type.value)
            return type;
      }

      return SessionTrigger.INVALID;
   }

   public short getValue() {
      return value;
   }


}
