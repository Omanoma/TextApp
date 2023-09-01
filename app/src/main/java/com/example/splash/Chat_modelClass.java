package com.example.splash;

import java.util.Comparator;
import java.util.Date;

public class Chat_modelClass {
    public static final int Layout1 = 1;
    public static final int Layout2 = 2;

    private int viewType;
    private String message;
    private int image;
    public Date date;

    public Chat_modelClass(int viewType,String message,int image, Date date){
        this.viewType = viewType;
        this.message = message;
        this.image = image;
        this.date = date;
    }

    public int getViewType() {
        return viewType;
    }

    public String getMessage() {
        return message;
    }

    public int getImage(){return image;}
}
class SortbyDate implements Comparator<Chat_modelClass>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Chat_modelClass a, Chat_modelClass b)
    {
        return b.date.compareTo(a.date);
    }
}
