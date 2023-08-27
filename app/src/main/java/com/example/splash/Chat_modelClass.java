package com.example.splash;

public class Chat_modelClass {
    public static final int Layout1 = 1;
    public static final int Layout2 = 2;

    private int viewType;
    private String message;
    private int image;

    public Chat_modelClass(int viewType,String message,int image){
        this.viewType = viewType;
        this.message = message;
        this.image = image;
    }

    public int getViewType() {
        return viewType;
    }

    public String getMessage() {
        return message;
    }

    public int getImage(){return image;}
}
