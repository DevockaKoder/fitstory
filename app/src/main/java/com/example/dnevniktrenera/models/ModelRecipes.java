package com.example.dnevniktrenera.models;

public class ModelRecipes {

    private String nameRecipe, calories, recipe, id, type;

    public ModelRecipes () {
    }

    public ModelRecipes (String nameRecipe, String calories, String recipe, String id, String type) {
        this.nameRecipe = nameRecipe;
        this.calories = calories;
        this.type = type;
        this.recipe = recipe;
        this.id = id;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public void setNameRecipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}