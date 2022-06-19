package com.example.dnevniktrenera.models;

public class MenuZavtrak {
    String DayWeek, calories, name, recipe, type;

    public String getDayWeek() {return DayWeek;}

    public void setDayWeek(String dayWeek) {DayWeek = dayWeek;}

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
