package com.ppx.music.mvvm.test;

import androidx.databinding.BaseObservable;

/**
 * @Author Shirley
 * @Date：2024/10/8
 * @Desc：
 */
public class User extends BaseObservable {
    private String account;
    private String pwd;

    public User() {
    }

    public User(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        notifyChange();//通知xml数据改变 所有参数都会改变
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}