package com.example.pulopo.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.LoginRequest;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;
import com.example.pulopo.model.response.UserResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText edtmail, edtPass;


    private static final int request_Code = 12;
    LocationManager locationManager;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ApiServer apiServer;


    private void createRequestPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        } else {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                String[] permissionLog = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissionLog, request_Code);

            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == request_Code) {
            if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Truy cập địa chỉ đã bật", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng bật truy cập địa chỉ", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        createRequestPermission();
        notifyTurnOnLocation();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (locationManager.isLocationEnabled()) {
                        Login();
                    } else {
                        Toast.makeText(LoginActivity.this, "Bật định vị di động và khởi động lại ứng dụng", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
    }


    private void notifyTurnOnLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (!locationManager.isLocationEnabled()) {
                Toast.makeText(this, "Vui lòng bật định vị và khởi động lại ứng dụng", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void Login() {

        String email_tr, pass_str;
        email_tr = edtmail.getText().toString().trim();
        pass_str = edtPass.getText().toString().trim();
        LoginRequest loginRequest = new LoginRequest(email_tr, pass_str);
        if (TextUtils.isEmpty(email_tr) && TextUtils.isEmpty(pass_str)) {
            Toast.makeText(this, "Vui lòng nhập email, mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable.add(apiServer.login(loginRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {

                                UserResponse userResponse = userModel;
                                if (userResponse.getSuccess()) {
                                    UserUtil.setEmail(userResponse.getData().getEmail());
                                    UserUtil.setUserName(userResponse.getData().getUserName());
                                    UserUtil.setHoTen(userResponse.getData().getHoTen());
                                    UserUtil.setPassword(userResponse.getData().getPassword());
                                    UserUtil.setId((int) userResponse.getData().getid());
                                    Intent intent = new Intent(LoginActivity.this, ConnectFriendsActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Log.e("BUG", throwable.getMessage());
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }


    }


    public void init() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtmail = findViewById(R.id.edtMail);
        edtPass = findViewById(R.id.inputTextPassword);
    }
}