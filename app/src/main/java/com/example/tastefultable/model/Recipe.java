package com.example.tastefultable.model;

public class Recipe {
    private int id;
    private String name;
    private String img_url;
    private String time;

    // Constructor of Recipe Model
    public Recipe(int id, String name, String img_url, String time) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.time = time;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTime() {
        return time;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
