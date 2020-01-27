package com.example.happyrunning.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SPUtils;
import com.example.happyrunning.MyApplication;
import com.example.happyrunning.R;
import com.example.happyrunning.base.BasicFragment;
import com.example.happyrunning.commons.utils.Status_sp;
import com.example.happyrunning.commons.utils.Utils;
import com.example.happyrunning.db.DataManager;
import com.example.happyrunning.db.RealmHelper;
import com.example.happyrunning.ui.activity.Friend;
import com.example.happyrunning.ui.activity.Login;
import com.example.happyrunning.ui.activity.StepHistoryActivity;

import java.util.Objects;

public class Setting_Fragment extends BasicFragment {
    RelativeLayout set_pwd;
    RelativeLayout set_run;
    RelativeLayout set_walk;
    RelativeLayout set_friend;
    Button set_logout;
   DataManager dataManager = new DataManager(new RealmHelper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting,container,false);
        set_logout=view.findViewById(R.id.bt_logout);
        set_pwd=view.findViewById(R.id.set_pwd);
        set_run=view.findViewById(R.id.set_run);
        set_walk=view.findViewById(R.id.set_walk);
        set_friend=view.findViewById(R.id.set_friend);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        set_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        set_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(StepHistoryActivity.class,null);
            }
        });
        set_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getContext(),"功能开发中···");
//                startActivity(SportRecordDetailsActivity.class,null);
            }
        });
        set_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getContext(),"功能开发中···");
            }
        });
        set_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Friend.class,null);
            }
        });
    }
    private void logOut() {
        SPUtils.getInstance().put(Status_sp.ISLOGIN, false);

        dataManager.deleteSportRecord();

        MyApplication.exitActivity();
        Utils.showToast(getContext(), "退出登陆成功!");

        Intent it = new Intent(getContext(), Login.class);
        Objects.requireNonNull(getContext()).startActivity(it);
    }
}



