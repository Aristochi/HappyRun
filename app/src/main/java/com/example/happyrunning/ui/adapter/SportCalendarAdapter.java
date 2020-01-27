package com.example.happyrunning.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.happyrunning.R;
import com.example.happyrunning.commons.bean.PathRecord;
import com.example.happyrunning.sport_motion.MotionUtils;

import java.text.DecimalFormat;
import java.util.List;


public class SportCalendarAdapter extends BaseQuickAdapter<PathRecord, BaseViewHolder> {

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private DecimalFormat intFormat = new DecimalFormat("#");

    public SportCalendarAdapter(int layoutResId, @Nullable List<PathRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PathRecord item) {
        helper.setText(R.id.distance, decimalFormat.format(item.getmDistance() / 1000d));
        helper.setText(R.id.duration, MotionUtils.formatseconds(item.getmDuration()));
        helper.setText(R.id.calorie, intFormat.format(item.getmCalorie()));
    }
}
