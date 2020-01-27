package com.example.happyrunning.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.example.happyrunning.MyApplication;
import com.example.happyrunning.R;
import com.example.happyrunning.commons.utils.Status_sp;
import com.example.happyrunning.commons.utils.UIhelper;
import com.example.happyrunning.commons.utils.Utils;
import com.example.happyrunning.ui.BaseActivity;
import com.example.happyrunning.ui.permission.PermissionHelper;
import com.example.happyrunning.ui.permission.PermissionListener;
import com.example.happyrunning.ui.weight.CountDownProgress;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

public class Splash extends BaseActivity {
    @BindView(R.id.img_url)
    ImageView img_url;
    @BindView(R.id.countDownProgressView)
    CountDownProgress countDownProgress;
    @BindView(R.id.versions)
    TextView versions;

    /**
     * 上一次点击返回键时间
     */
    private long lastBackPressed;

    /*
    上次点击返回键时间
     */
    private  static final int QUIT_INTERVAL=3000;

//    申请权限
    private static String[] PERMISSIONS_STORAGE=
        {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (ImmersionBar.hasNavigationBar(this)) {
            ImmersionBar.with(this).transparentNavigationBar().init();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        img_url.setImageResource(R.mipmap.splash_bg);

        versions.setText(UIhelper.getString(R.string.splash_appversionname, MyApplication.getAppVersionName()));
        showToast("初始化中，请稍后...");
        countDownProgress.setTimeMillis(2000);
        countDownProgress.setProgressType(CountDownProgress.ProgressType.COUNT_BACK);
        countDownProgress.start();
    }

    @Override
    public void initListener() {
        countDownProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownProgress.stop();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 获取权限
                    PermissionHelper.requestPermissions(Splash.this, PERMISSIONS_STORAGE, new PermissionListener() {
                        @Override
                        public void onPassed() {
                            startActivity();
                        }
                    });
                } else {
                    Splash.this.startActivity();
                }
            }
        });

        countDownProgress.setProgressListener(new CountDownProgress.OnProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (progress==0) {
                    // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 获取权限
                        PermissionHelper.requestPermissions(Splash.this, PERMISSIONS_STORAGE, Splash.this.getResources().getString(R.string.app_name) + "需要获取存储、位置权限", new PermissionListener() {
                            @Override
                            public void onPassed() {
                                startActivity();
                            }
                        });
                    } else {
                        Splash.this.startActivity();
                    }
                }
            }
        });
    }

    public void startActivity() {
        if (SPUtils.getInstance().getBoolean(Status_sp.ISLOGIN)) {
            startActivity(new Intent(Splash.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(Splash.this, Login.class));
            finish();
        }
        countDownProgress.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { // 表示按返回键 时的操作
                long backPressed = System.currentTimeMillis();
                if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                    lastBackPressed = backPressed;
                    Utils.showToast(Splash.this, "再按一次退出");
                } else {
                    if (countDownProgress != null) {
                        countDownProgress.stop();
                        countDownProgress.clearAnimation();
                    }
                    moveTaskToBack(false);
                    MyApplication.closeApp(this);
                    finish();
                }
            }
        }
        return false;
    }


}
