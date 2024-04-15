package com.example.pulopo.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;
import com.example.pulopo.model.response.RegisterReponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RegisterActivity extends AppCompatActivity {
    EditText textPersonName, textEmailAddress, textHoVaTen, textPasswordConfirm;
    Button btnRegister,btnBack;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ApiServer apiServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatAccountUser();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void creatAccountUser() {
        String email_str,perSonUser_str,hoVaTen,passConfirm_str;
        email_str = textEmailAddress.getText().toString().trim();
        perSonUser_str = textPersonName.getText().toString().trim();
        hoVaTen = textHoVaTen.getText().toString().trim();
        passConfirm_str = textPasswordConfirm.getText().toString().trim();
        if(TextUtils.isEmpty(email_str) || TextUtils.isEmpty(perSonUser_str)
                || TextUtils.isEmpty(hoVaTen) || TextUtils.isEmpty(passConfirm_str)){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else{
            System.out.println(email_str+perSonUser_str+hoVaTen+passConfirm_str);
            compositeDisposable.add(apiServer.register(perSonUser_str,passConfirm_str,hoVaTen,email_str)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                RegisterReponse registerReponse = userModel;
                                if(registerReponse.getSuccess()){
                                    UserUtil.setUserName(perSonUser_str);
                                    UserUtil.setPassword(passConfirm_str);
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));



        }



    }
    private void initView() {
        textEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        textPersonName = findViewById(R.id.editTextPersonName);
        textHoVaTen = findViewById(R.id.editHoVaTen);
        textPasswordConfirm= findViewById(R.id.editTextTextPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBackToLogin);
    }
}