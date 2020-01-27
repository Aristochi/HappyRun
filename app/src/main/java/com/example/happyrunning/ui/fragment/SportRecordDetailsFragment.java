package com.example.happyrunning.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.example.happyrunning.R;
import com.example.happyrunning.commons.bean.PathRecord;
import com.example.happyrunning.sport_motion.MotionUtils;
import com.example.happyrunning.ui.BaseFragment;
import com.example.happyrunning.ui.activity.SportRecordDetailsActivity;

import java.text.DecimalFormat;

import butterknife.BindView;

/**
 * 描述: 运动记录详情-详情

 */
public class SportRecordDetailsFragment extends BaseFragment {

    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tvSpeed)
    TextView tvSpeed;
    @BindView(R.id.tvDistribution)
    TextView tvDistribution;
    @BindView(R.id.tvCalorie)
    TextView tvCalorie;

    private PathRecord pathRecord = null;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private DecimalFormat intFormat = new DecimalFormat("#");

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sportrecorddetails;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pathRecord = bundle.getParcelable(SportRecordDetailsActivity.SPORT_DATA);
        }

        if (null != pathRecord) {
            tvDistance.setText(decimalFormat.format(pathRecord.getmDistance() / 1000d));
            tvDuration.setText(MotionUtils.formatseconds(pathRecord.getmDuration()));
            tvSpeed.setText(decimalFormat.format(pathRecord.getmSpeed()));
            tvDistribution.setText(decimalFormat.format(pathRecord.getmDistribution()));
            tvCalorie.setText(intFormat.format(pathRecord.getmCalorie()));
        }

    }

    @Override
    public void initListener() {

    }
}
