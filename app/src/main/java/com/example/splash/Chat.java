package com.example.splash;

import static io.grpc.okhttp.internal.Platform.logger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {
    TextView username;
    RecyclerView recyclerView;
    List<Chat_modelClass> list;
    Chat_Adapter adapter;
    EditText message;
    ShapeableImageView icon;
    String otherUser;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        icon = findViewById(R.id.icons);
        username = findViewById(R.id.usernameChat);
        Bundle b = getIntent().getExtras();
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.chatSpace);
        getExtraInfo(b);
        setMessagerLayout();



    }
    public void sendMessage(View v){
        String b = message.getText().toString();
        if(b.equals("")){
            Toast toast=Toast. makeText(this,"Type something",Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            list.add(new Chat_modelClass(Chat_modelClass.Layout1,b,R.mipmap.face1));
            message = findViewById(R.id.input2);
            adapter = new Chat_Adapter(list,Chat.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            recyclerView.getLayoutManager().scrollToPosition(list.size()-1);
            adapter.notifyDataSetChanged();
            Toast toast=Toast. makeText(this,"Sended",Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    public void setMessagerLayout(){
        message = findViewById(R.id.input2);
        adapter = new Chat_Adapter(list,Chat.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void getExtraInfo(Bundle b){
        icon.setImageResource((Integer) b.get("Image"));
        username.setText(b.getString("User"));
        otherUser = b.getString("OTHERUserID","NULL");
        currentUser = b.getString("CURRENTUSERID","NULL");
        logger.info(otherUser+" OZIOMA2");
        logger.info(currentUser+" OZIOMA3");
    }

}