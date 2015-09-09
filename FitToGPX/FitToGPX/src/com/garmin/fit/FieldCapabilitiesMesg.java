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


public class FieldCapabilitiesMesg extends Mesg {


   public FieldCapabilitiesMesg() {
      super(Factory.createMesg(MesgNum.FIELD_CAPABILITIES));
   }

   public FieldCapabilitiesMesg(final Mesg mesg) {
      super(mesg);
   }
   
   /**
    * Get message_index field
    *
    * @return message_index
    */
   public Integer getMessageIndex() {
      return getFieldIntegerValue(254);
   }

   /**
    * Set message_index field
    *
    * @param messageIndex
    */
   public void setMessageIndex(Integer messageIndex) {
      setFieldValue("message_index", messageIndex);
   }   
   /**
    * Get file field
    *
    * @return file
    */
   public File getFile() {
      Short value = getFieldShortValue(0);
     if (value == null)
        return null;
      return File.getByValue(value);
   }

   /**
    * Set file field
    *
    * @param file
    */
   public void setFile(File file) {
      setFieldValue("file", file.value);
   }   
   /**
    * Get mesg_num field
    *
    * @return mesg_num
    */
   public Integer getMesgNum() {
      return getFieldIntegerValue(1);
   }

   /**
    * Set mesg_num field
    *
    * @param mesgNum
    */
   public void setMesgNum(Integer mesgNum) {
      setFieldValue("mesg_num", mesgNum);
   }   
   /**
    * Get field_num field
    *
    * @return field_num
    */
   public Short getFieldNum() {
      return getFieldShortValue(2);
   }

   /**
    * Set field_num field
    *
    * @param fieldNum
    */
   public void setFieldNum(Short fieldNum) {
      setFieldValue("field_num", fieldNum);
   }   
   /**
    * Get count field
    *
    * @return count
    */
   public Integer getCount() {
      return getFieldIntegerValue(3);
   }

   /**
    * Set count field
    *
    * @param count
    */
   public void setCount(Integer count) {
      setFieldValue("count", count);
   }

}
