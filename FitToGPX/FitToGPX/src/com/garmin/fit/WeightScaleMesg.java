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


public class WeightScaleMesg extends Mesg {


   public WeightScaleMesg() {
      super(Factory.createMesg(MesgNum.WEIGHT_SCALE));
   }

   public WeightScaleMesg(final Mesg mesg) {
      super(mesg);
   }
   
   /**
    * Get timestamp field
    * Units: s
    *
    * @return timestamp
    */
   public DateTime getTimestamp() {
      return timestampToDateTime(getFieldLongValue(253));
   }

   /**
    * Set timestamp field
    * Units: s
    *
    * @param timestamp
    */
   public void setTimestamp(DateTime timestamp) {
      setFieldValue("timestamp", timestamp.getTimestamp());
   }   
   /**
    * Get weight field
    * Units: kg
    *
    * @return weight
    */
   public Float getWeight() {
      return getFieldFloatValue(0);
   }

   /**
    * Set weight field
    * Units: kg
    *
    * @param weight
    */
   public void setWeight(Float weight) {
      setFieldValue("weight", weight);
   }   
   /**
    * Get percent_fat field
    * Units: %
    *
    * @return percent_fat
    */
   public Float getPercentFat() {
      return getFieldFloatValue(1);
   }

   /**
    * Set percent_fat field
    * Units: %
    *
    * @param percentFat
    */
   public void setPercentFat(Float percentFat) {
      setFieldValue("percent_fat", percentFat);
   }   
   /**
    * Get percent_hydration field
    * Units: %
    *
    * @return percent_hydration
    */
   public Float getPercentHydration() {
      return getFieldFloatValue(2);
   }

   /**
    * Set percent_hydration field
    * Units: %
    *
    * @param percentHydration
    */
   public void setPercentHydration(Float percentHydration) {
      setFieldValue("percent_hydration", percentHydration);
   }   
   /**
    * Get visceral_fat_mass field
    * Units: kg
    *
    * @return visceral_fat_mass
    */
   public Float getVisceralFatMass() {
      return getFieldFloatValue(3);
   }

   /**
    * Set visceral_fat_mass field
    * Units: kg
    *
    * @param visceralFatMass
    */
   public void setVisceralFatMass(Float visceralFatMass) {
      setFieldValue("visceral_fat_mass", visceralFatMass);
   }   
   /**
    * Get bone_mass field
    * Units: kg
    *
    * @return bone_mass
    */
   public Float getBoneMass() {
      return getFieldFloatValue(4);
   }

   /**
    * Set bone_mass field
    * Units: kg
    *
    * @param boneMass
    */
   public void setBoneMass(Float boneMass) {
      setFieldValue("bone_mass", boneMass);
   }   
   /**
    * Get muscle_mass field
    * Units: kg
    *
    * @return muscle_mass
    */
   public Float getMuscleMass() {
      return getFieldFloatValue(5);
   }

   /**
    * Set muscle_mass field
    * Units: kg
    *
    * @param muscleMass
    */
   public void setMuscleMass(Float muscleMass) {
      setFieldValue("muscle_mass", muscleMass);
   }   
   /**
    * Get basal_met field
    * Units: kcal/day
    *
    * @return basal_met
    */
   public Float getBasalMet() {
      return getFieldFloatValue(7);
   }

   /**
    * Set basal_met field
    * Units: kcal/day
    *
    * @param basalMet
    */
   public void setBasalMet(Float basalMet) {
      setFieldValue("basal_met", basalMet);
   }   
   /**
    * Get physique_rating field
    *
    * @return physique_rating
    */
   public Short getPhysiqueRating() {
      return getFieldShortValue(8);
   }

   /**
    * Set physique_rating field
    *
    * @param physiqueRating
    */
   public void setPhysiqueRating(Short physiqueRating) {
      setFieldValue("physique_rating", physiqueRating);
   }   
   /**
    * Get active_met field
    * Units: kcal/day
    * Comment: ~4kJ per kcal, 0.25 allows max 16384 kcal
    *
    * @return active_met
    */
   public Float getActiveMet() {
      return getFieldFloatValue(9);
   }

   /**
    * Set active_met field
    * Units: kcal/day
    * Comment: ~4kJ per kcal, 0.25 allows max 16384 kcal
    *
    * @param activeMet
    */
   public void setActiveMet(Float activeMet) {
      setFieldValue("active_met", activeMet);
   }   
   /**
    * Get metabolic_age field
    * Units: years
    *
    * @return metabolic_age
    */
   public Short getMetabolicAge() {
      return getFieldShortValue(10);
   }

   /**
    * Set metabolic_age field
    * Units: years
    *
    * @param metabolicAge
    */
   public void setMetabolicAge(Short metabolicAge) {
      setFieldValue("metabolic_age", metabolicAge);
   }   
   /**
    * Get visceral_fat_rating field
    *
    * @return visceral_fat_rating
    */
   public Short getVisceralFatRating() {
      return getFieldShortValue(11);
   }

   /**
    * Set visceral_fat_rating field
    *
    * @param visceralFatRating
    */
   public void setVisceralFatRating(Short visceralFatRating) {
      setFieldValue("visceral_fat_rating", visceralFatRating);
   }   
   /**
    * Get user_profile_index field
    * Comment: Associates this weight scale message to a user.  This corresponds to the index of the user profile message in the weight scale file.
    *
    * @return user_profile_index
    */
   public Integer getUserProfileIndex() {
      return getFieldIntegerValue(12);
   }

   /**
    * Set user_profile_index field
    * Comment: Associates this weight scale message to a user.  This corresponds to the index of the user profile message in the weight scale file.
    *
    * @param userProfileIndex
    */
   public void setUserProfileIndex(Integer userProfileIndex) {
      setFieldValue("user_profile_index", userProfileIndex);
   }

}
