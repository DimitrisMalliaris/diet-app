package com.malliaris_traga.dietplanner;

public class Food {
    private String foodid = "apple";
    private String foodName = "apple";
    private float caloriesPerServing = 0;
    private float carbsPerServing = 0;
    private float proteinPerServing = 0;
    private float fatPerServing = 0;

    public Food(){

    }

    public Food(String foodid, float caloriesPerServing){
        this.foodid = foodid;
        this.caloriesPerServing = caloriesPerServing;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public float getCaloriesPerServing() {
        return caloriesPerServing;
    }

    public void setCaloriesPerServing(float caloriesPerServing) {
        this.caloriesPerServing = caloriesPerServing;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getCarbsPerServing() {
        return carbsPerServing;
    }

    public void setCarbsPerServing(float carbsPerServing) {
        this.carbsPerServing = carbsPerServing;
    }

    public float getProteinPerServing() {
        return proteinPerServing;
    }

    public void setProteinPerServing(float proteinPerServing) {
        this.proteinPerServing = proteinPerServing;
    }

    public float getFatPerServing() {
        return fatPerServing;
    }

    public void setFatPerServing(float fatPerServing) {
        this.fatPerServing = fatPerServing;
    }
}