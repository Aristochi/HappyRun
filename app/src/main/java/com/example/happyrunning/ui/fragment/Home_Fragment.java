package com.example.happyrunning.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.example.happyrunning.R;
import com.example.happyrunning.commons.bean.PathRecord;
import com.example.happyrunning.commons.bean.SportMotionRecord;
import com.example.happyrunning.commons.utils.DateUtils;
import com.example.happyrunning.commons.utils.LogUtils;
import com.example.happyrunning.commons.utils.Status_sp;
import com.example.happyrunning.commons.utils.UIhelper;
import com.example.happyrunning.db.DataManager;
import com.example.happyrunning.db.RealmHelper;
import com.example.happyrunning.sport_motion.MotionUtils;
import com.example.happyrunning.ui.activity.SportRecordDetailsActivity;
import com.example.happyrunning.ui.adapter.SportCalendarAdapter;
import com.example.happyrunning.ui.weight.FullyLinearLayoutManager;
import com.example.happyrunning.ui.weight.custom.CustomWeekBar;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

public class Home_Fragment extends Fragment {



    TextView tvTitle;

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RecyclerView mRecyclerView;
   ImageView imgCurrent;
    CalendarLayout mCalendarLayout;
    @BindView(R.id.sport_achievement)
    LinearLayout sport_achievement;
    private int mYear;

    private Dialog tipDialog = null;
    private SportCalendarAdapter adapter;
    private List<PathRecord> sportList = new ArrayList<>(0);

    private DataManager dataManager = null;

    private final int SPORT = 0x0012;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_home,container,false);
        tvTitle=view.findViewById(R.id.tv_title);
        mTextMonthDay=view.findViewById(R.id.tv_month_day);
        mTextYear=view.findViewById(R.id.tv_year);
        mTextLunar=view.findViewById(R.id.tv_lunar);
        mTextCurrentDay=view.findViewById(R.id.tv_current_day);
        mCalendarView=view.findViewById(R.id.calendarView);
        mRecyclerView=view.findViewById(R.id.recyclerView);
        mCalendarLayout=view.findViewById(R.id.calendarLayout);
        sport_achievement=view.findViewById(R.id.sport_achievement);
        imgCurrent=view.findViewById(R.id.img_current);
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imgCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });

        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                mTextLunar.setVisibility(View.VISIBLE);
                mTextYear.setVisibility(View.VISIBLE);
                mTextMonthDay.setText(UIhelper.getString(R.string.date_month_day, calendar.getMonth(), calendar.getDay()));
                mTextYear.setText(String.valueOf(calendar.getYear()));
                mTextLunar.setText(calendar.getLunar());
                mYear = calendar.getYear();

                getSports(DateUtils.formatStringDateShort(calendar.getYear(), calendar.getMonth(), calendar.getDay()));

                LogUtils.d("onDateSelected", "  -- " + calendar.getYear() +
                        "  --  " + calendar.getMonth() +
                        "  -- " + calendar.getDay() +
                        "  --  " + isClick + "  --   " + calendar.getScheme());
            }
        });
        mCalendarView.setOnYearChangeListener(year -> {
            mTextMonthDay.setText(String.valueOf(year));
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            SportRecordDetailsActivity.StartActivity(getActivity(), sportList.get(position));
        });


    }




    public void initData()
    {
        dataManager = new DataManager(new RealmHelper());
        mYear = mCalendarView.getCurYear();
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mTextMonthDay.setText(UIhelper.getString(R.string.date_month_day, mCalendarView.getCurMonth(), mCalendarView.getCurDay()));
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
        mCalendarView.setWeekStarWithSun();
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(mRecyclerView, LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.line)));
        adapter = new SportCalendarAdapter(R.layout.adapter_sportcalendar, sportList);
        mRecyclerView.setAdapter(adapter);
        mCalendarView.setWeekBar(CustomWeekBar.class);
        upDateUI();
    }
    private void upDateUI() {
        loadSportData();

        getSports(DateUtils.formatStringDateShort(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), mCalendarView.getCurDay()));
    }

    private void loadSportData() {
        try {
            List<SportMotionRecord> records = dataManager.queryRecordList(Integer.parseInt(SPUtils.getInstance().getString(Status_sp.USERID, "0")));
            if (null != records) {
                Map<String, Calendar> map = new HashMap<>();
                for (SportMotionRecord record : records) {
                    String dateTag = record.getDateTag();
                    String[] strings = dateTag.split("-");
                    int year = Integer.parseInt(strings[0]);
                    int month = Integer.parseInt(strings[1]);
                    int day = Integer.parseInt(strings[2]);
                    map.put(getSchemeCalendar(year, month, day, 0xFFCC0000, "记").toString(),
                            getSchemeCalendar(year, month, day, 0xFFCC0000, "记"));
                }
                //此方法在巨大的数据量上不影响遍历性能，推荐使用
                mCalendarView.setSchemeDate(map);
            }
        } catch (Exception e) {
            LogUtils.e("获取运动数据失败", e);
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
//        calendar.addScheme(0xFF008800, text);
        return calendar;
    }

    private void getSports(String dateTag) {
        try {
            List<SportMotionRecord> records = dataManager.queryRecordList(Integer.parseInt(SPUtils.getInstance().getString(Status_sp.USERID, "0")), dateTag);
            if (null != records) {

                sportList.clear();
                adapter.notifyDataSetChanged();

                for (SportMotionRecord record : records) {
                    PathRecord pathRecord = new PathRecord();
                    pathRecord.setId(record.getId());
                    pathRecord.setmDistance(record.getDistance());
                    pathRecord.setmDuration(record.getDuration());
                    pathRecord.setmPathLinePoints(MotionUtils.parseLatLngLocations(record.getPathLine()));
                    pathRecord.setmStartPoint(MotionUtils.parseLatLngLocation(record.getStratPoint()));
                    pathRecord.setmEndPoint(MotionUtils.parseLatLngLocation(record.getEndPoint()));
                    pathRecord.setmStartTime(record.getmStartTime());
                    pathRecord.setmEndTime(record.getmEndTime());
                    pathRecord.setmCalorie(record.getCalorie());
                    pathRecord.setmSpeed(record.getSpeed());
                    pathRecord.setmDistribution(record.getDistribution());
                    pathRecord.setmDateTag(record.getDateTag());
                    sportList.add(pathRecord);
                }
                if (sportList.isEmpty())
                    sport_achievement.setVisibility(View.GONE);
                else
                    sport_achievement.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            } else {
                sport_achievement.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e("获取运动数据失败", e);
            sport_achievement.setVisibility(View.GONE);
        }
    }
    protected class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = mSpace;
            outRect.left = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            } else {
                outRect.top = 0;
            }

        }

        SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case SPORT:
                upDateUI();
                break;
            default:
                break;
        }
    }

}
