package com.boot.web;

import java.util.List;

public class Response<T> {
    private List<T> result;
    private int status;
    private String message;

    public Response(List<T> result, int status, String message) {
        this.result = result;
        this.status = status;
        this.message = message;
    }

    public Response(){}

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
