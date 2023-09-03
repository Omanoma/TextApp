package com.example.splash;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static io.grpc.okhttp.internal.Platform.logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {
    TextView username;
    RecyclerView recyclerView;
    List<Chat_modelClass> list;
    Chat_Adapter adapter;
    EditText message;
    int image;
    ShapeableImageView icon;
    String otherUser;
    String currentUser;
    FirebaseFirestore db;
    HashMap<String, Boolean> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_chat);
        dates = new HashMap<>();
        icon = findViewById(R.id.icons);
        username = findViewById(R.id.usernameChat);
        Bundle b = getIntent().getExtras();
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.chatSpace);
        getExtraInfo(b);
        setMessagerLayout();
        //getReceiverMessage();
        //getSenderMessage();
        list.sort(new SortbyDate());
        // Attach a Firestore snapshot listener to the chat collectio
        db.collection("Chat").whereEqualTo("SenderID",otherUser).whereEqualTo("RecieverID",currentUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        for(DocumentChange docchange : value.getDocumentChanges()){
                            if(docchange.getType() == DocumentChange.Type.ADDED) {
                                System.out.println("addd" + docchange.getType());
                                QueryDocumentSnapshot doc = docchange.getDocument();
                                SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/YYYY");
                                logger.info(doc.getDate("Date")+" Ozioma123");
                                boolean g = dates.containsKey(formatter.format(doc.getDate("Date")));
                                if (!g) {
                                    dates.put(formatter.format(doc.getDate("Date")), true);
                                    list.add(new Chat_modelClass(Chat_modelClass.Layout3, doc.getDate("Date")));
                                }

                                list.add(new Chat_modelClass(Chat_modelClass.Layout2, doc.getString("Messages"), image, doc.getDate("Date")));
                            }
                        }
                        list.sort(new SortbyDate());
                        adapter = new Chat_Adapter(list,Chat.this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyItemRangeInserted(list.size(),list.size());
                        recyclerView.smoothScrollToPosition(list.size()-1);

                    }

                });
        db.collection("Chat").whereEqualTo("SenderID",currentUser).whereEqualTo("RecieverID",otherUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                for(DocumentChange docchange : value.getDocumentChanges()){
                    if(docchange.getType() == DocumentChange.Type.ADDED){
                        System.out.println("addd" + docchange.getType());
                    QueryDocumentSnapshot doc = docchange.getDocument();
                        SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/YYYY");
                        boolean g = dates.containsKey(formatter.format(doc.getDate("Date")));
                        if (!g) {
                            dates.put(formatter.format(doc.getDate("Date")), true);
                            list.add(new Chat_modelClass(Chat_modelClass.Layout3, doc.getDate("Date")));
                        }

                        list.add(new Chat_modelClass(Chat_modelClass.Layout1, doc.getString("Messages"), image, doc.getDate("Date")));                }
                }
                list.sort(new SortbyDate());
                adapter = new Chat_Adapter(list,Chat.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
                recyclerView.setAdapter(adapter);
                adapter.notifyItemRangeInserted(list.size(),list.size());
                recyclerView.smoothScrollToPosition(list.size()-1);

        }



    });
    }
    public void sendMessage(View v){
        String b = message.getText().toString();
        if(b.equals("")){
            Toast toast=Toast. makeText(this,"Type something",Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Map<String,Object> chats = new HashMap<>();
            chats.put("SenderID",currentUser);
            chats.put("RecieverID", otherUser);
            chats.put("Messages",b);
            chats.put("Date",new Date());

            db.collection("Chat").add(chats);
            adapter.notifyDataSetChanged();
            message.setText(null);
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
        image = (Integer) b.get("Image");
        username.setText(b.getString("User"));
        otherUser = b.getString("OTHERUserID","NULL");
        currentUser = b.getString("CURRENTUSERID","NULL");
        logger.info(otherUser+" OZIOMA2");
        logger.info(currentUser+" OZIOMA3");
    }
}
