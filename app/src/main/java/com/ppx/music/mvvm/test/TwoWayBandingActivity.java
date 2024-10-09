package com.ppx.music.mvvm.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.ppx.music.R;
import com.ppx.music.databinding.ActivityTowWayBindingBinding;

/**
 * @Author Shirley
 * @Date：2024/10/9
 * @Desc：databinding 双向绑定
 */
public class TwoWayBandingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTowWayBindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tow_way_binding);
        TestViewModel viewModel = new TestViewModel();
        User user = new User("shirley1", "123456");
        viewModel.getUser().setValue(user);

        MutableLiveData<User> data = viewModel.getUser();
        data.observe(this, user1 -> binding.setViewModel(viewModel));


        binding.btnLogin.setOnClickListener(view -> {
            if (TextUtils.isEmpty(viewModel.getUser().getValue().getAccount())) {
                Toast.makeText(TwoWayBandingActivity.this, "TwoWayBanding 账号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(viewModel.getUser().getValue().getPwd())) {
                Toast.makeText(TwoWayBandingActivity.this, "TwoWayBanding 密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(TwoWayBandingActivity.this, "TwoWayBanding 登录成功", Toast.LENGTH_SHORT).show();
        });
    }
}