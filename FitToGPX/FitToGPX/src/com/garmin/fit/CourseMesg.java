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


public class CourseMesg extends Mesg {


   public CourseMesg() {
      super(Factory.createMesg(MesgNum.COURSE));
   }

   public CourseMesg(final Mesg mesg) {
      super(mesg);
   }
   
   /**
    * Get sport field
    *
    * @return sport
    */
   public Sport getSport() {
      Short value = getFieldShortValue(4);
     if (value == null)
        return null;
      return Sport.getByValue(value);
   }

   /**
    * Set sport field
    *
    * @param sport
    */
   public void setSport(Sport sport) {
      setFieldValue("sport", sport.value);
   }   
   /**
    * Get name field
    *
    * @return name
    */
   public String getName() {
      return getFieldStringValue(5);
   }

   /**
    * Set name field
    *
    * @param name
    */
   public void setName(String name) {
      setFieldValue("name", name);
   }   
   /**
    * Get capabilities field
    *
    * @return capabilities
    */
   public Long getCapabilities() {
      return getFieldLongValue(6);
   }

   /**
    * Set capabilities field
    *
    * @param capabilities
    */
   public void setCapabilities(Long capabilities) {
      setFieldValue("capabilities", capabilities);
   }

}
