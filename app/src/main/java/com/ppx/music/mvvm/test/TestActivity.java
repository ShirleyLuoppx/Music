package com.ppx.music.mvvm.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ppx.music.R;
import com.ppx.music.databinding.ActivityTestBinding;

/**
 * @Author Shirley
 * @Date：2024/10/8
 * @Desc：有点奇怪，这个类里面什么也没做，但是在模拟器中旋转屏幕后编辑框中的内容是不会消失的，并且新建了一个空的activity 也是这种情况；
 * 但是在另外一个项目Test中，在activity中的编辑框内容旋转是会被丢失的......
 */
public class TestActivity extends AppCompatActivity {

    private TestViewModel testViewModel;
    private EditText etPwd;
    private EditText etAccount;
    private Button btnLogin;
    private TextView tvAccount;
    private TextView tvPwd;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);

        //将activity和viewmodel 绑定起来
//        testViewModel = new ViewModelProvider(this).get(TestViewModel.class);

//        etPwd = findViewById(R.id.et_password);
//        etAccount = findViewById(R.id.et_account);
//        btnLogin = findViewById(R.id.btn_login);
//        tvAccount = findViewById(R.id.tv_account);
//        tvPwd = findViewById(R.id.tv_pwd);

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                testViewModel.account.postValue(etAccount.getText().toString().trim()); //setValue只能在主线程中调用，postValue可以在任意线程中调用
//                testViewModel.pwd.postValue(etPwd.getText().toString().trim());

                //这里，如果上面设置值的时候用的是postValue，那么这里getvalue获取的就空了，可以将判断拿到下面observe中去
//                if (TextUtils.isEmpty(testViewModel.pwd.getValue()) || TextUtils.isEmpty(testViewModel.account.getValue())) {
//                    Toast.makeText(TestActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
//                    Log.d("TAG", "onClick: 账号密码不能为空");
//                } else {
//                    Toast.makeText(TestActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    Log.d("TAG", "onClick: 登录成功");
//                }
//            }
//        });

//        //observe 观察account的变化
//        testViewModel.account.observe(this, account -> {
//            if(TextUtils.isEmpty(account)){
//                Toast.makeText(TestActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
//            }
//            tvAccount.setText("账号:"+account);
//        });
//
//        testViewModel.pwd.observe(this, pwd -> {
//            tvPwd.setText("密码:"+pwd);
//            if(TextUtils.isEmpty(pwd)){
//                Toast.makeText(TestActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(TestActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//            }
//        });

        setTextDataByDataBinding();
    }

    private User user;
    private void setTextDataByDataBinding(){
        //通过DataBindingUtil来设置界面
        //DataBindingUtil会根据布局文件生成一个对应的绑定类ActivityTestBinding，这个类里面包含了布局中所有试图的引用
        //生成的ActivityTestBinding 是根据布局文件命名而定的，如果布局文件叫做 LoginActivity，那么生成的类就叫ActivityLoginBinding
        ActivityTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        user = new User("admin1", "1234566");
        //绑定数据
        binding.setUser(user);

        binding.btnLogin.setOnClickListener(view -> {
            user.setAccount(binding.etAccount.getText().toString().trim());
            user.setPwd(binding.etPassword.getText().toString().trim());
        });

    }
}