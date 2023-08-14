package com.example.splash;

public class Chat_modelClass {
    public static final int Layout1 = 1;
    public static final int Layout2 = 2;

    private int viewType;
    private String message;

    public Chat_modelClass(int viewType,String message){
        this.viewType = viewType;
        this.message = message;
    }

    public int getViewType() {
        return viewType;
    }

    public String getMessage() {
        return message;
    }
}
