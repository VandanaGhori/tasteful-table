package com.example.tastefultable.model;

public class Ingredients {
    private int id;
    private int rec_id;
    private String name;
    private String qty;
    private String img_url;

    public Ingredients(int id, int rec_id, String name, String qty, String img_url) {
        this.id = id;
        this.rec_id = rec_id;
        this.name = name;
        this.qty = qty;
        this.img_url = img_url;
    }

    public int getId() {
        return id;
    }

    public int getRec_id() {
        return rec_id;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
