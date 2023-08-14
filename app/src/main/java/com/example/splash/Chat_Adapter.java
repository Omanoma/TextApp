package com.example.splash;

import static com.example.splash.Chat_modelClass.Layout1;
import static com.example.splash.Chat_modelClass.Layout2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (lis.get(position).getViewType()){
            case Layout1: String sm = lis.get(position).getMessage();
                ((SenderMessageViewHolder)holder).setView(sm);
                break;
            case Layout2: String rm = lis.get(position).getMessage();
                ((ReceiverMessageViewHolder)holder).setView(rm);
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
    public SenderMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_sm = itemView.findViewById(R.id.SText);
    }
    public void setView(String text){
        tv_sm.setText(text);
    }
}

class ReceiverMessageViewHolder extends RecyclerView.ViewHolder{
    private TextView tv_rm;
    public ReceiverMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_rm = itemView.findViewById(R.id.RText);
    }
    public void setView(String text){
        tv_rm.setText(text);
    }
}
