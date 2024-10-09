package com.ppx.music.mvvm.test;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @Author Shirley
 * @Date：2024/10/8
 * @Desc：
 */
public class TestViewModel extends ViewModel {

    MutableLiveData<User> user;

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }
}