package com.hrithik.chatt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Messages> arrayList;

    public MessagesAdapter(Context context, ArrayList<Messages> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Messages msg = arrayList.get(viewType);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msg.getSenderId())) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_lyout_item, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages msg = arrayList.get(position);
        Messages nxtMsg = new Messages();
        if (position != 0)
            nxtMsg = arrayList.get(position - 1);

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            if (position != 0 && nxtMsg.getSenderId().equals(msg.getSenderId())) {
                viewHolder.msgContinued.setText(msg.getMessage());
                viewHolder.msgContinued.setVisibility(View.VISIBLE);
                viewHolder.msg.setVisibility(View.GONE);
            } else
                viewHolder.msg.setText(msg.getMessage());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            if (position != 0 && nxtMsg.getSenderId().equals(msg.getSenderId())) {
                viewHolder.msgContinued.setText(msg.getMessage());
                viewHolder.msgContinued.setVisibility(View.VISIBLE);
                viewHolder.msg.setVisibility(View.GONE);
            } else
                viewHolder.msg.setText(msg.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView msg, msgContinued;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msgSend);
            msgContinued = itemView.findViewById(R.id.msgSendContinued);
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView msg, msgContinued;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msgReceive);
            msgContinued = itemView.findViewById(R.id.msgReceiveContinued);
        }
    }
}
