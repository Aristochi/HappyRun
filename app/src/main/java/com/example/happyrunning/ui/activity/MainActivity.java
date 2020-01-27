package com.example.happyrunning.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.happyrunning.MyApplication;
import com.example.happyrunning.R;
import com.example.happyrunning.ui.adapter.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{

    /**
     * 上次点击返回键的时间
     */
    private long lastBackPressed;

    //上次点击返回键的时间
    public static final int QUIT_INTERVAL = 2500;
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR=3;
    private MyFragmentPagerAdapter mAdapter;
    private RadioGroup tabs_rg;
    private RadioButton rb_settings;
    private RadioButton rb_running;
    private RadioButton rb_home;
    private RadioButton rb_walk;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager());


        bindViews();

        rb_home.setChecked(true);
    }

public void bindViews()
{   rb_walk=(RadioButton)findViewById(R.id.walk_tab);
    rb_home=(RadioButton)findViewById(R.id.home_tab);
    rb_running=(RadioButton)findViewById(R.id.run_tab);
    rb_settings=(RadioButton)findViewById(R.id.settings_tab);
    tabs_rg=(RadioGroup)findViewById(R.id.tabs_rg);
    tabs_rg.setOnCheckedChangeListener(this);
    viewPager=(ViewPager)findViewById(R.id.fragment_vp);
    viewPager.setAdapter(mAdapter);
    viewPager.setCurrentItem(0);
    viewPager.addOnPageChangeListener(this);

}
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId)
        {
            case R.id.home_tab:
                viewPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.run_tab:
                viewPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.walk_tab:
                viewPager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.settings_tab:
                viewPager.setCurrentItem(PAGE_FOUR);
                break;

        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if (i==2)
        {
            switch (viewPager.getCurrentItem())
            {
                case PAGE_ONE:
                    rb_home.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_running.setChecked(true);

                    break;
                case PAGE_THREE:
                  rb_walk.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rb_settings.setChecked(true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) { // 表示按返回键 时的操作
                long backPressed = System.currentTimeMillis();
                if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                    lastBackPressed = backPressed;
                    Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    moveTaskToBack(false);
                    MyApplication.closeApp(this);
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
