package com.example.tastefultable.model;

public class Favourite {
    private int id;
    private int user_id;
    private int recipe_id;

    public Favourite(int user_id, int recipe_id) {
        this.user_id = user_id;
        this.recipe_id = recipe_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }
}
