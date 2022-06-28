package com.malliaris_traga.dietplanner;

import java.util.ArrayList;
import java.util.List;

public class DiaryEntry {
    private String Date = "1111-01-01";
    private float AbdomenInCM = 0;
    private float NeckInCM = 0;
    private float WaistInCM = 0;
    private float HipInCM = 0;
    private float WeightInKG = 0;
    private float WaterInLiters = 0;
    private float PhysicalActivity = 1.2f; // minimal 1.2 - 1.95 heavy workouts
    private float TotalDailyCalories = 0;

    public DiaryEntry(){

    }

    public DiaryEntry(String Date, float AbdomenInCM, float NeckInCM, float WaistInCM, float HipInCM, float WeightInKG, float WaterInLiters, float PhysicalActivity, float TotalDailyCalories){
        this.Date = Date;
        this.AbdomenInCM = AbdomenInCM;
        this.NeckInCM = NeckInCM;
        this.WaistInCM = WaistInCM;
        this.HipInCM = HipInCM;
        this.WeightInKG = WeightInKG;
        this.WaterInLiters = WaterInLiters;
        this.PhysicalActivity = PhysicalActivity;
        this.TotalDailyCalories = TotalDailyCalories;
    }

    public float getTotalDailyCalories(){
        return  TotalDailyCalories;
    }

    public void setTotalDailyCalories(float totalDailyCalories){
        this.TotalDailyCalories = totalDailyCalories;
    }

    public void setFoodEntries(float totalDailyCalories) {
        this.TotalDailyCalories = totalDailyCalories;
    }

    public float getAbdomenInCM() {
        return AbdomenInCM;
    }

    public void setAbdomenInCM(float abdomenInCM) {
        this.AbdomenInCM = abdomenInCM;
    }

    public float getNeckInCM() {
        return NeckInCM;
    }

    public void setNeckInCM(float neckInCM) {
        this.NeckInCM = neckInCM;
    }

    public float getWaistInCM() {
        return WaistInCM;
    }

    public void setWaistInCM(float waistInCM) {
        this.WaistInCM = waistInCM;
    }

    public float getHipInCM() {
        return HipInCM;
    }

    public void setHipInCM(float hipInCM) {
        this.HipInCM = hipInCM;
    }

    public float getWeightInKG() {
        return WeightInKG;
    }

    public void setWeightInKG(float weightInKG) {
        this.WeightInKG = weightInKG;
    }

    public float getWaterInLiters() {
        return WaterInLiters;
    }

    public void setWaterInLiters(float waterInLiters) {
        this.WaterInLiters = waterInLiters;
    }

    public float getPhysicalActivity() {
        return PhysicalActivity;
    }

    public void setPhysicalActivity(float physicalActivity) {
        this.PhysicalActivity = physicalActivity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
