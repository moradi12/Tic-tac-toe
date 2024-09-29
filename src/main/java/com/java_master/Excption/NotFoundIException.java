package com.java_master.Excption;

public class NotFoundIException extends Exception {
    private String massage;

    public NotFoundIException(String massage) {
        this.massage = massage;
    }

    public String getMassage() {
        return massage;

    }
}
