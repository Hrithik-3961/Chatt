package com.hrithik.chatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView user_name;
    private EditText editMessage;
    private ImageView sendBtn, back;
    private RecyclerView recyclerView;

    private String name, receiverUid, senderUid;
    private String senderRoom, receiverRoom;
    private ArrayList<Messages> arrayList;
    private MessagesAdapter messagesAdapter;

    private ViewModel viewModel;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication(), getIntent().getLongExtra("id", -1))).get(ViewModel.class);
        database = FirebaseDatabase.getInstance();

        name = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = FirebaseAuth.getInstance().getUid();
        arrayList = new ArrayList<>();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        user_name = findViewById(R.id.name);
        editMessage = findViewById(R.id.message);
        sendBtn = findViewById(R.id.sendBtn);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        messagesAdapter = new MessagesAdapter(this, arrayList);
        recyclerView.setAdapter(messagesAdapter);

        user_name.setText(name);

        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");
        /*chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages msg = dataSnapshot.getValue(Messages.class);
                    arrayList.add(msg);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        viewModel.getMessages().observe(this, new Observer<List<Messages>>() {
            @Override
            public void onChanged(List<Messages> messages) {
                arrayList.clear();
                arrayList.addAll(messages);
                messagesAdapter.notifyDataSetChanged();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString().trim();
                if(!message.isEmpty()) {
                    editMessage.setText(null);
                    Date date = new Date();
                    final Messages msg = new Messages(message, senderUid, date.getTime());

                    database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(msg)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    database.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(msg)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });
                }
            }
        });

    }
}