package com.example.pulopo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.UtilsCommon;
import com.example.pulopo.interfaceClick.ItemClickListener;
import com.example.pulopo.model.ChatMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessageList;
    private  String sendid;
    ApiServer apiServer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;
    private static final int TYPE_IMG = 3;
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
        if(viewType == TYPE_IMG){
            view = LayoutInflater.from(context).inflate(R.layout.item_img_rec,parent,false);
            return new ImageViewHolder(view);
        }
        if(viewType == TYPE_FILE){
            view = LayoutInflater.from(context).inflate(R.layout.item_file_read,parent,false);
            return new FileViewHolder(view);
        }
        if(viewType == TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.item_send_mess,parent,false);
            return new SendMessViewHolder(view);

        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.item_received,parent,false);
            return new ReceviedViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Kieu du lieu file
        if(getItemViewType(position) == TYPE_FILE){
            int tempPosition = position;
            ( (FileViewHolder) holder).txtmess.setText("Đã gửi tập tin");
            ( (FileViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);

            ((FileViewHolder) holder).setItemClickListener(new ItemClickListener() {

                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(isLongClick){
                        //tai file ve local
                        downloadFile(tempPosition);
                    }
                }
            });
        }
        // neu kieu du lieu la anh
        if(getItemViewType(position) == TYPE_IMG){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String imgpath = "images/"+chatMessageList.get(position).mess;
            StorageReference storageRef = storage.getReference().child(imgpath);
            int tempPosition = position;
            try {
                File localFile = File.createTempFile("image", "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // File đã được tải thành công
                        // Bạn có thể sử dụng localFile để thực hiện các thao tác khác, chẳng hạn như hiển thị nó trong ImageView
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        if (bitmap != null) {
                            ((ImageViewHolder) holder).imgReceived.setImageBitmap(bitmap);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Xử lý lỗi khi không thể tải file từ Firebase Storage
                    }
                });
                localFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageViewHolder) holder).setItemClickListener(new ItemClickListener() {

                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(isLongClick){
                        //tai file ve local
                        downloadImg(tempPosition);
                    }
                }
            });
            ((ImageViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
        }
        // kieu du lieu tin nhan gui di
        if(getItemViewType(position) == TYPE_SEND){

            if(chatMessageList.get(position).typeMess==1){
                ( (SendMessViewHolder) holder).txtmess.setText("Đã chia sẻ một vị trí");
            }
            if(chatMessageList.get(position).typeMess==0){
                ( (SendMessViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            }
            ( (SendMessViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
            String chatId = chatMessageList.get(position).chatId;
            ((SendMessViewHolder) holder).setItemClickListener(new ItemClickListener() {

                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(isLongClick){
                        deleteMess(chatId);
                    }
                }
            });
        }
        // kieu du lieu tin nhan nhan ve
        else{
            if(chatMessageList.get(position).typeMess==1){
                ( (ReceviedViewHolder) holder).txtmess.setText("Đã chia sẻ một vị trí");
                ( (ReceviedViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);

            }
            if(chatMessageList.get(position).typeMess==0){
                ( (ReceviedViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
                ( (ReceviedViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            }

        }
    }

    private void downloadFile(int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String imgpath = "files/"+chatMessageList.get(position).mess;
        StorageReference storageRef = storage.getReference().child(imgpath);

        String localFilePath = "/storage/emulated/0/Download/Pulopo/";
        String fileName = chatMessageList.get(position).mess.replace(","," ")
                .replace("-", " ")
                .replace(":", " ")
                .trim().replaceAll("\\s+", "");
        File localDirectory = new File(localFilePath,fileName);
        storageRef.getFile(localDirectory).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Đã tải tập tin", Toast.LENGTH_LONG).show();
            }
        });


    }
    private void downloadImg(int tempPosition) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String imgpath = "images/"+chatMessageList.get(tempPosition).mess;
        StorageReference storageRef = storage.getReference().child(imgpath);

        String localFilePath = "/storage/emulated/0/Download/Pulopo/";
        String fileName = chatMessageList.get(tempPosition).mess.replace(","," ")
                .replace("-", " ")
                .replace(":", " ")
                .trim().replaceAll("\\s+", "");
        File localDirectory = new File(localFilePath,fileName+".jpg");

        storageRef.getFile(localDirectory).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("down","tải về thành công");
                Toast.makeText(context, "Đã tải ảnh", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void deleteMess(String chatid) {
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        compositeDisposable.add(apiServer.delete(chatid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            Toast.makeText(context, "Đã xóa tin nhắn", Toast.LENGTH_LONG).show();
                        },
                        throwable -> {
                            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(chatMessageList.get(position).getTypeMess() == 2){

            return TYPE_IMG;
        }
        if(chatMessageList.get(position).getTypeMess() == 3){
            Log.e("logFile",String.valueOf(chatMessageList.get(position).getTypeMess()));
            return TYPE_FILE;
        }
        if(chatMessageList.get(position).sendid.equals(sendid)){
            return TYPE_SEND;
        } else{
            return TYPE_RECEIVE;
        }



    }

    public void clearData() {
        chatMessageList.clear();
    }
    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView  txttime;
        ImageView imgReceived;
        ItemClickListener itemClickListener;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgReceived = itemView.findViewById(R.id.imgReceived);
            txttime = itemView.findViewById(R.id.timesendimg);
            itemView.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
        }
    }
    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtmess, txttime;
        ItemClickListener itemClickListener;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.fileRead);
            txttime = itemView.findViewById(R.id.timeSendFile);
            itemView.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
        }
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtmess, txttime;
        ItemClickListener itemClickListener;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmessSend);
            txttime = itemView.findViewById(R.id.txttimeSend);
            itemView.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
        }
    }
    class ReceviedViewHolder extends RecyclerView.ViewHolder{
        TextView txtmess, txttime;
        public ReceviedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmessrece);
            txttime = itemView.findViewById(R.id.txttimerece);
        }
    }


}