package com.ppx.music.mvvm.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ppx.music.MusicApplication;
import com.ppx.music.R;

/**
 * @Author Shirley
 * @Date：2024/10/8
 * @Desc：
 */
public class NormalActivity extends AppCompatActivity {

    private EditText etPwd;
    private EditText etAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        Log.d("TAG", "NormalActivity onCreate: ");

        etPwd = findViewById(R.id.et_password);
        etAccount = findViewById(R.id.et_account);
        Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();

                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(account)) {
                    Toast.makeText(NormalActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MusicApplication.context, "登录成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}