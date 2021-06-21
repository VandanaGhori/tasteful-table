package com.example.tastefultable.model;

public class Preparations {
    private int id;
    private int rec_id;
    private String steps;
    private int order_no;

    public Preparations(int id, int rec_id, String steps, int order_no) {
        this.id = id;
        this.rec_id = rec_id;
        this.steps = steps;
        this.order_no = order_no;
    }

    public int getId() {
        return id;
    }

    public int getRec_id() {
        return rec_id;
    }

    public String getSteps() {
        return steps;
    }

    public int getOrder_no() {
        return order_no;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRec_id(int rec_id) {
        this.rec_id = rec_id;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setOrder_no(int order_no) {
        this.order_no = order_no;
    }
}
