package com.hrithik.chatt;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        database = FirebaseDatabase.getInstance();

        name = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = FirebaseAuth.getInstance().getUid();
        arrayList = new ArrayList<>();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;
        viewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication(), senderRoom)).get(ViewModel.class);
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
        recyclerView.setHasFixedSize(true);
        messagesAdapter = new MessagesAdapter(this, arrayList, getWindowManager());
        recyclerView.setAdapter(messagesAdapter);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom && messagesAdapter.getItemCount() != 0)
                    recyclerView.smoothScrollToPosition(messagesAdapter.getItemCount() - 1);
            }
        });

        user_name.setText(name);

        viewModel.getMessages().observe(this, new Observer<UserMessages>() {

            @Override
            public void onChanged(UserMessages userMessages) {
                if (userMessages != null) {
                    arrayList.clear();
                    arrayList.addAll(userMessages.getMsg());
                    messagesAdapter.notifyDataSetChanged();
                    if (messagesAdapter.getItemCount() != 0)
                        recyclerView.scrollToPosition(messagesAdapter.getItemCount() - 1);
                }
            }
        });

        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages msg = dataSnapshot.getValue(Messages.class);
                    arrayList.add(msg);
                }
                UserMessages senderMsg = new UserMessages(arrayList, senderRoom);
                UserMessages receiverMsg = new UserMessages(arrayList, receiverRoom);
                viewModel.insertMessage(senderMsg);
                viewModel.insertMessage(receiverMsg);
                messagesAdapter.notifyItemInserted(arrayList.size());
                if (messagesAdapter.getItemCount() != 0)
                    recyclerView.scrollToPosition(messagesAdapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    editMessage.setText(null);
                    Date date = new Date();
                    final Messages msg = new Messages(message, senderUid, date.getTime());
                    arrayList.add(msg);
                    messagesAdapter.notifyItemInserted(arrayList.size());
                    UserMessages senderMsg = new UserMessages(arrayList, senderRoom);
                    viewModel.insertMessage(senderMsg);
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