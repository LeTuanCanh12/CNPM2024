package com.example.pulopo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulopo.R;
import com.example.pulopo.interfaceClick.ItemClickListener;
import com.example.pulopo.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessageList;
    private String sendid;
    private static final int TYPE_FILE = 4;

    public ChatAdapter(Context context, List<ChatMessage> chatMessageList, String sendid) {
        this.context = context;
        this.chatMessageList = chatMessageList;
        this.sendid = sendid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_FILE) {
            // Inflate the layout for a FileViewHolder from the item_file_read XML layout file.
            view = LayoutInflater.from(context).inflate(R.layout.item_file_read, parent, false);
            return new FileViewHolder(view);
        }
        return null;
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FILE) {
            ((FileViewHolder) holder).txtmess.setText("Đã gửi tập tin");
            ((FileViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtmess, txttime;
        ItemClickListener itemClickListener;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.fileRead);
            txttime = itemView.findViewById(R.id.timeSendFile);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
        }
    }

}
