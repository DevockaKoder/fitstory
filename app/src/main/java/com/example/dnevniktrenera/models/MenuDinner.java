package com.example.dnevniktrenera.models;


public class MenuDinner {
    String calories, name, recipe, type;

    public MenuDinner() {};

    public MenuDinner(String calories, String name, String recipe, String type) {
        this.calories = calories;
        this.name = name;
        this.recipe = recipe;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
