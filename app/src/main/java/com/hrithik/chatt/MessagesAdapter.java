package com.hrithik.chatt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> arrayList;
    private final int ITEM_SEND = 1;
    private final int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_lyout_item, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages msg = arrayList.get(position);
        Messages nxtMsg = new Messages();
        if(position != 0)
        nxtMsg = arrayList.get(position-1);

        if(holder.getClass() == SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            if(position != 0 && nxtMsg.getSenderId().equals(msg.getSenderId())) {
                viewHolder.msgContinued.setText(msg.getMessage());
                viewHolder.msgContinued.setVisibility(View.VISIBLE);
                viewHolder.msg.setVisibility(View.GONE);
            }
            else
                viewHolder.msg.setText(msg.getMessage());
        }
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            if(position != 0 && nxtMsg.getSenderId().equals(msg.getSenderId())) {
                viewHolder.msgContinued.setText(msg.getMessage());
                viewHolder.msgContinued.setVisibility(View.VISIBLE);
                viewHolder.msg.setVisibility(View.GONE);
            }
            else
            viewHolder.msg.setText(msg.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages msg = arrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msg.getSenderId()))
            return ITEM_SEND;
        else
            return ITEM_RECEIVE;
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView msg, msgContinued;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msgSend);
            msgContinued = itemView.findViewById(R.id.msgSendContinued);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView msg, msgContinued;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msgReceive);
            msgContinued = itemView.findViewById(R.id.msgReceiveContinued);
        }
    }
}
