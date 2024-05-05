package com.example.pulopo.Activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.pulopo.Adapter.UserAdapter;
import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;
import com.example.pulopo.model.User;
import com.example.pulopo.model.response.ListChatUser;
import com.example.pulopo.model.response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


//Useracticity
public class ConnectFriendsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    ImageView search,infor;
    ApiServer apiServer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_friends);
        initView();
        initEvent();
        initToolbar();
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        getUserFromFire();

    }

    private void initEvent() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnectFriendsActivity.this,SearchingActivity.class);
                startActivity(intent);
            }
        });
        infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnectFriendsActivity.this,InfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserFromFire() {
        compositeDisposable.add(apiServer.getUsers(UserUtil.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {

                            List<User> userList = new ArrayList<>();
                            ListChatUser listChatUser = userModel;
                            if(listChatUser.getSuccess()){
                                for(int i =0;i <listChatUser.getData().size();i++) {
                                    User user = new User();
                                    user.setId((int)listChatUser.getData().get(i).getUserId());
                                    user.setUsername(listChatUser.getData().get(i).getUserName());
                                    userList.add(user);

                                }
                                if(userList.size()>0){
                                    userAdapter = new UserAdapter(getApplicationContext(),userList);
                                    recyclerView.setAdapter(userAdapter);
                                }
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));


    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_user);
        search = findViewById(R.id.imgsearch);
        infor = findViewById(R.id.imgeditprofile);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }
}