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


public class MesgCapabilitiesMesg extends Mesg {


   public MesgCapabilitiesMesg() {
      super(Factory.createMesg(MesgNum.MESG_CAPABILITIES));
   }

   public MesgCapabilitiesMesg(final Mesg mesg) {
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
    * Get count_type field
    *
    * @return count_type
    */
   public MesgCount getCountType() {
      Short value = getFieldShortValue(2);
     if (value == null)
        return null;
      return MesgCount.getByValue(value);
   }

   /**
    * Set count_type field
    *
    * @param countType
    */
   public void setCountType(MesgCount countType) {
      setFieldValue("count_type", countType.value);
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
   /**
    * Get num_per_file field
    *
    * @return num_per_file
    */
   public Integer getNumPerFile() {
      return getFieldIntegerValue("num_per_file");
   }

   /**
    * Set num_per_file field
    *
    * @param numPerFile
    */
   public void setNumPerFile(Integer numPerFile) {
      setFieldValue("num_per_file", numPerFile);
   }   
   /**
    * Get max_per_file field
    *
    * @return max_per_file
    */
   public Integer getMaxPerFile() {
      return getFieldIntegerValue("max_per_file");
   }

   /**
    * Set max_per_file field
    *
    * @param maxPerFile
    */
   public void setMaxPerFile(Integer maxPerFile) {
      setFieldValue("max_per_file", maxPerFile);
   }   
   /**
    * Get max_per_file_type field
    *
    * @return max_per_file_type
    */
   public Integer getMaxPerFileType() {
      return getFieldIntegerValue("max_per_file_type");
   }

   /**
    * Set max_per_file_type field
    *
    * @param maxPerFileType
    */
   public void setMaxPerFileType(Integer maxPerFileType) {
      setFieldValue("max_per_file_type", maxPerFileType);
   }

}
