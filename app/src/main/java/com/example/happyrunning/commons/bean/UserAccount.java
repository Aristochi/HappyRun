package com.example.happyrunning.commons.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 描述: 账号密码
 */
public class UserAccount extends RealmObject implements Serializable {

    @PrimaryKey
    private Long id;

    //用户名
    @Required
    private String account;

    //密码
    @Required
    private String psd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPsd() {
        return psd;
    }

    public void setPsd(String psd) {
        this.psd = psd;
    }
}
