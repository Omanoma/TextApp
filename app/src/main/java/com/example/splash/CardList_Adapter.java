package com.example.splash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardList_Adapter extends RecyclerView.Adapter<CardList_Adapter.MyViewHolder>{
    Context context;
    List<User> a;

    private final ItemInterface item;

    public CardList_Adapter(Context con, List<User> a, ItemInterface item){
        this.context = con;
        this.a = a;
        this.item = item;
    }

    @NonNull
    @Override
    public CardList_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card,parent,false);
        return new CardList_Adapter.MyViewHolder(v,item);
    }

    @Override
    public void onBindViewHolder(@NonNull CardList_Adapter.MyViewHolder holder, int position) {
        holder.icon.setImageResource(a.get(position).getImage());
        holder.name.setText(a.get(position).username);
    }

    @Override
    public int getItemCount() {
        return a.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;
        public MyViewHolder(@NonNull View itemView,ItemInterface item) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.Name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(item != null){
                        int pos = getAdapterPosition();
                        item.onItemClick(pos);
                    }
                }
            });

        }
    }
}
