package com.example.happyrunning;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.example.happyrunning.commons.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MyApplication  extends Application {
    public static boolean DEBUG_MODE = true;//是否是DEBUG模式
    private static MyApplication applicationContext;
    private static Handler handler;

    public static List<Activity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        DEBUG_MODE = BuildConfig.DEBUG_MODE;
        applicationContext = this;

        //初始化数据库
        Realm.init(getInstance());
//        new SecureRandom().nextBytes(Utils.getRealmKey(key));

        //在子线程中完成其他初始化
        initApplication();
    }

    private void initApplication() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                Utils.init(getInstance());

                //内存泄漏检测放到最后执行
                if (LeakCanary.isInAnalyzerProcess(getInstance())) {
                    return;
                }
//                LeakCanary.install(getInstance());
            }
        });
    }

    public static Application getInstance() {
        return applicationContext;
    }

    public static Handler getHandler() {
        if (null == handler)
            handler = new Handler(Looper.getMainLooper());
        return handler;
    }

    public static void addActivity(Activity activity) {
        if (null != activity && !activityList.contains(activity))
            activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (null != activity && activityList.contains(activity)) {
            activityList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    public static void exitActivity() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeApp(Context context) {

        if (null != handler)
            handler.removeCallbacksAndMessages(null);

        exitActivity();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            PackageManager pm = applicationContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(applicationContext.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            LogUtils.e("VersionInfo Exception", e);
        }
        return versionName;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode() {
        int versioncode = 0;
        try {
            PackageManager pm = applicationContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(applicationContext.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            LogUtils.e("VersionInfo Exception", e);
        }
        return versioncode;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getInstance());
    }

}
