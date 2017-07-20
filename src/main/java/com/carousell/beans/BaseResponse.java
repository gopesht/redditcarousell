package com.carousell.beans;

/**
 * Created by a1dmiuxe(gopesh.tulsyan) on 19/07/17.
 */

public class BaseResponse {
    private Object response;
    private String message;


    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
