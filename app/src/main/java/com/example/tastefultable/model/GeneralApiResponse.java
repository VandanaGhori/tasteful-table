package com.example.tastefultable.model;

public class GeneralApiResponse<T> {
    boolean success;
    String error;
    int error_code;

    T data;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public int getError_code() {
        return error_code;
    }

    public T getData() {
        return data;
    }
}
