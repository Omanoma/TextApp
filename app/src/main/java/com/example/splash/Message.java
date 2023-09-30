package com.example.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Message extends Fragment implements ItemInterface{

    List<User> user;
    String currentUser;
    ImageView icon;

    public Message() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        icon = v.findViewById(R.id.icon2);
        icon.setClickable(true);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePop p = new ProfilePop();
                p.show(getParentFragmentManager(),"THE");
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        User a = new User();
        currentUser = a.getCurrentUser();
        RecyclerView r = view.findViewById(R.id.recycle);
        CompletableFuture<List<User>> futureUserList = a.getAllContact();
        futureUserList.thenAccept(users -> {
            user = users;
            CardList_Adapter c = new CardList_Adapter(getContext(), user,this);
            r.setAdapter(c);
            r.setLayoutManager(new LinearLayoutManager(getContext()));

            System.out.println(user + " kill 2");
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(),Chat.class);
        intent.putExtra("Image",user.get(position).image);
        intent.putExtra("User",user.get(position).username);
        intent.putExtra("OTHERUserID",user.get(position).userID);
        intent.putExtra("CURRENTUSERID",currentUser);
        startActivity(intent);
    }



}