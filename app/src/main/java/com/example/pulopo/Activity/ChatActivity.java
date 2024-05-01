package com.example.pulopo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    int iduser;
    String username;
    RecyclerView recyclerView;
    ImageView imgSend, attachFile;
    EditText edtMess;
    ApiServer apiServer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int PICK_FILE_REQUEST = 2;
    StorageReference storageReference;

    /**
     * This method is called when the activity is starting.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Retrieve the 'id' extra from the intent that started this activity. If it doesn't exist, default to 0.
        iduser = getIntent().getIntExtra("id", 0);

        // Retrieve the 'username' extra from the intent that started this activity.
        username = getIntent().getStringExtra("username");

        // Initialize the view components of this activity
        initView();

        // Initialize the toolbar for this activity
        initToolbar();

        // Create an instance of the ApiServer interface using the Retrofit client
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);

        // Get a reference to the root of the Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    /**
     * This method is called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it. The resultCode will be RESULT_CANCELED if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // The user has successfully picked an file
            Uri selectedFileUri = data.getData();
            uploadFileToFirebase(selectedFileUri);
            Log.e("sendFile", "rs");
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarChat);
        toolbar.setTitle(username);
    }

    /**
     * This method is used to initialize the view components of the ChatActivity.
     * It sets up the RecyclerView for displaying chat messages and the EditText for inputting text.
     * It also sets up the ImageView for attaching files, and assigns a click listener to it.
     */
    private void initView() {
        // Find the RecyclerView in the layout and assign it to the recyclerView variable
        recyclerView = findViewById(R.id.recycleview_chat);

        // Find the EditText in the layout and assign it to the edtMess variable
        edtMess = findViewById(R.id.edtinputtext);

        // Create a new LinearLayoutManager and set it as the layout manager for the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the RecyclerView to have a fixed size to improve performance
        recyclerView.setHasFixedSize(true);

        // Find the ImageView for attaching files in the layout and assign it to the attachFile variable
        attachFile = findViewById(R.id.attachFile);

        // Set a click listener on the ImageView
        // When the ImageView is clicked, the openFilePicker() method is called
        attachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("dd MMM, yyyy- hh:mm a", new Locale("en", "US")).format(date);
    }

    // attach file

    /**
     * This method is used to open a file picker dialog.
     * It creates an intent with the action `Intent.ACTION_GET_CONTENT` which allows the user to select any type of file.
     * The selected file is then returned to the calling activity's `onActivityResult()` method.
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Set the type of files that can be selected to all types
        intent.setType("*/*");

        // Start an activity with the intent created and a request code `PICK_FILE_REQUEST`
        startActivityForResult(intent, PICK_FILE_REQUEST);

        Log.e("sendFile", "run");
    }

    /**
     * This method is used to upload a file to Firebase.
     * It creates a reference to a new file in the Firebase Storage with a unique name based on the current date.
     *
     * @param fileUri The Uri of the file to be uploaded.
     */
    private void uploadFileToFirebase(Uri fileUri) {
        // Create a reference to a new file in the Firebase Storage
        StorageReference fileRef = storageReference.child("files/" + "pulopo_file_" + formatDate(new Date()));

        // Get the name of the file
        String fileName = fileRef.getName();

        // Start the file upload
        UploadTask uploadTask = fileRef.putFile(fileUri);

        // Add a success listener to the upload task
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // On successful upload, send the file to the server
            sendFileToServer(fileName);
            Log.e("sendFile", "up firebase");
        }).addOnFailureListener(exception -> {
            Toast.makeText(getApplicationContext(), "Gửi không thành công, vui lòng thử lại",
                    Toast.LENGTH_LONG).show();

        });
    }

    /**
     * This method is used to send a message to the server with the name of the uploaded file.
     *
     * @param fileName The name of the file to be sent.
     */
    private void sendFileToServer(String fileName) {
        // Add the send operation to the composite disposable
        compositeDisposable.add(apiServer.sendMessChat(UserUtil.getId(), iduser, fileName, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            // On successful send, log a message and display a toast
                            Log.e("sendFile", "up sv");
                            Toast.makeText(getApplicationContext(), "Đã gửi tập tin", Toast.LENGTH_LONG).show();
                        },
                        throwable -> {
                            // On error, display a toast with the error message
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }
}
