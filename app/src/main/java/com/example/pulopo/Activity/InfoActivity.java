package com.example.pulopo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pulopo.R;
import com.example.pulopo.Retrofit.ApiServer;
import com.example.pulopo.Retrofit.RetrofitClient;
import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.Utils.UtilsCommon;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InfoActivity extends AppCompatActivity {
    ApiServer apiServer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText userName, fullName, email;
    Button btn_update;
    Button btn_logout;
    Button btn_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        apiServer = RetrofitClient.getInstance(UtilsCommon.BASE_URL).create(ApiServer.class);
        initView();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Gọi phương thức gốc để hoạt động mặc định của nút "Back"
        // Các xử lý khác nếu cần
        Intent intent = new Intent(InfoActivity.this,ConnectFriendsActivity.class);
        startActivity(intent);
    }
    private void initView() {
        userName = findViewById(R.id.info_name);
        fullName = findViewById(R.id.info_fullname);
        email = findViewById(R.id.info_email);

        userName.setText(UserUtil.getUserName());
        fullName.setText(UserUtil.getHoTen());
        email.setText(UserUtil.getEmail());

        btn_update = findViewById(R.id.btn_update);
        btn_logout = findViewById(R.id.btn_logout);
        btn_delete = findViewById(R.id.btn_delete);

        btn_update.setOnClickListener(view -> {
            Intent intent = new Intent(InfoActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(view -> {
            showLogoutConfirmationDialog();
        });

        btn_delete.setOnClickListener(view -> {
            showDeleteAccountConfirmationDialog();
        });
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn Đăng xuất không?");

        builder.setPositiveButton("Có", (dialog, which) -> {
            // Xử lý khi người dùng chọn Có
            Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
            dialog.dismiss(); // Đóng dialog sau khi xử lý
        });

        builder.setNegativeButton("Không", (dialog, which) -> {
            // Xử lý khi người dùng chọn Không
            dialog.dismiss(); // Đóng dialog sau khi xử lý
        });

        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteAccountConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn xoá tài khoản không?");

        builder.setPositiveButton("Có", (dialog, which) -> {
            // Xử lý khi người dùng chọn Có
            compositeDisposable.add(apiServer.deleteAccount(UserUtil.getUserName())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                if (userModel.getSuccess()) {
                                    Toast.makeText(getApplicationContext(), "Xóa tài khoản "+UserUtil.getUserName()+" thành công", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(InfoActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));

            dialog.dismiss(); // Đóng dialog sau khi xử lý
        });

        builder.setNegativeButton("Không", (dialog, which) -> {
            // Xử lý khi người dùng chọn Không
            dialog.dismiss(); // Đóng dialog sau khi xử lý
        });

        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}