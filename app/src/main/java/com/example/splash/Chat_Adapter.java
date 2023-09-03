package com.example.splash;

import static com.example.splash.Chat_modelClass.Layout1;
import static com.example.splash.Chat_modelClass.Layout2;
import static com.example.splash.Chat_modelClass.Layout3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Chat_Adapter extends RecyclerView.Adapter {
    List<Chat_modelClass> lis;
    Context context;
    public Chat_Adapter(List<Chat_modelClass> lis,Context context1){
        this.lis = lis;
        this.context = context1;
    }

    @Override
    public int getItemViewType(int position) {
        switch (lis.get(position).getViewType()){
            case 1: return Layout1;
            case 2: return Layout2;
            case 3: return Layout3;
            default:return-1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType){
            case Layout1:
                View layoutOne = LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
                return new SenderMessageViewHolder(layoutOne);
            case Layout2:
                View layoutTwo = LayoutInflater.from(context).inflate(R.layout.reciever,parent,false);
                return new ReceiverMessageViewHolder(layoutTwo);
            case Layout3:
                View layoutThree = LayoutInflater.from(context).inflate(R.layout.date_chat_layout,parent,false);
                return new DateShowerViewHolder(layoutThree);
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (lis.get(position).getViewType()){
            case Layout1: String sm = lis.get(position).getMessage();
                ((SenderMessageViewHolder)holder).setView(sm,lis.get(position).getImage(),lis.get(position).date);
                break;
            case Layout2: String rm = lis.get(position).getMessage();
                ((ReceiverMessageViewHolder)holder).setView(rm,lis.get(position).getImage(),lis.get(position).date);
                break;
            case Layout3:((DateShowerViewHolder)holder).setDate(lis.get(position).date);
            break;


        }
    }

    @Override
    public int getItemCount() {
        return lis.size();
    }
}

class SenderMessageViewHolder extends RecyclerView.ViewHolder{
    private TextView tv_sm;
    private ImageView image;
    private TextView Sdate;
    public SenderMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_sm = itemView.findViewById(R.id.SText);
        image =  itemView.findViewById(R.id.imageView3);
        Sdate = itemView.findViewById(R.id.STime);
    }
    public void setView(String text, int images, Date d){
        image.setImageResource(images);
        tv_sm.setText(text);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Sdate.setText(formatter.format(d));

    }
}

class ReceiverMessageViewHolder extends RecyclerView.ViewHolder{
    private TextView tv_rm;
    private ImageView image;
    private TextView Rdate;
    public ReceiverMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_rm = itemView.findViewById(R.id.RText);
        image =  itemView.findViewById(R.id.imageView2);
        Rdate = itemView.findViewById(R.id.RTime);
    }
    public void setView(String text, int images, Date d){
        image.setImageResource(images);
        tv_rm.setText(text);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Rdate.setText(formatter.format(d));
    }
}
class DateShowerViewHolder extends RecyclerView.ViewHolder{

    TextView date;
    public DateShowerViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.Date);
    }
    public void setDate(Date d){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        date.setText(formatter.format(d));
    }
}
