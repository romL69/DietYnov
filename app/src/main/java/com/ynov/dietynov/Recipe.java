package com.ynov.dietynov;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {

    private String title;
    private int portion;
    private String picture;
    private Time time;
    private ArrayList<Step> step;
    private ArrayList<Ingredient> ingredient;
    private Nutrition nutrition;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }



    public ArrayList<Step> getStep() {
        return step;
    }

    public void setStep(ArrayList<Step> step) {
        this.step = step;
    }

    public ArrayList<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }
}
