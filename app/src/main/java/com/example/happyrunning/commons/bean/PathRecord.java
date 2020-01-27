package com.example.happyrunning.commons.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录轨迹起点、终点、距离等
 */
public class PathRecord implements Parcelable {

    //id
    private Long id;
    //运动起点
    private LatLng mStartPoint;
    //运动终点
    private  LatLng mEndPoint;
    //运动轨迹
    private List<LatLng>mPathLinePoints=new ArrayList<>();
    //运动距离
    private Double mDistance;
    //运动时长
    private Long mDuration;
    //运动开始时间
    private Long mStartTime;
    //运动结束时间
    private Long mEndTime;
    //消耗卡路里
    private Double mCalorie;
    //平均时速(公里/小时)
    private Double mSpeed;
    //平均配速(分钟/公里)
    private Double mDistribution;
    //日期标记
    private String mDateTag;


    public Double getmCalorie() {
        return mCalorie;
    }

    public Double getmDistance() {
        return mDistance;
    }

    public Double getmDistribution() {
        return mDistribution;
    }

    public Double getmSpeed() {
        return mSpeed;
    }

    public LatLng getmEndPoint() {
        return mEndPoint;
    }

    public LatLng getmStartPoint() {
        return mStartPoint;
    }

    public List<LatLng> getmPathLinePoints() {
        return mPathLinePoints;
    }

    public Long getId() {
        return id;
    }

    public Long getmDuration() {
        return mDuration;
    }

    public Long getmEndTime() {
        return mEndTime;
    }

    public Long getmStartTime() {
        return mStartTime;
    }

    public String getmDateTag() {
        return mDateTag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setmCalorie(Double mCalorie) {
        this.mCalorie = mCalorie;
    }

    public void setmDateTag(String mDateTag) {
        this.mDateTag = mDateTag;
    }

    public void setmDistance(Double mDistance) {
        this.mDistance = mDistance;
    }

    public void setmDistribution(Double mDistribution) {
        this.mDistribution = mDistribution;
    }

    public void setmDuration(Long mDuration) {
        this.mDuration = mDuration;
    }

    public void setmEndPoint(LatLng mEndPoint) {
        this.mEndPoint = mEndPoint;
    }

    public void setmEndTime(Long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setmPathLinePoints(List<LatLng> mPathLinePoints) {
        this.mPathLinePoints = mPathLinePoints;
    }

    public void setmSpeed(Double mSpeed) {
        this.mSpeed = mSpeed;
    }

    public void setmStartPoint(LatLng mStartPoint) {
        this.mStartPoint = mStartPoint;
    }

    public void setmStartTime(Long mStartTime) {
        this.mStartTime = mStartTime;
    }
    public void addpoint(LatLng point) {
        mPathLinePoints.add(point);
    }
    @Override
    public String toString() {
        StringBuilder record = new StringBuilder();
        record.append("recordSize:" + getmPathLinePoints().size() + ", ");
        record.append("distance:" + getmDistance() + "m, ");
        record.append("duration:" + getmDuration() + "s");
        return record.toString();
    }
  public PathRecord()
  {

  }
    protected PathRecord(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mStartPoint = in.readParcelable(AMapLocation.class.getClassLoader());
        this.mEndPoint = in.readParcelable(AMapLocation.class.getClassLoader());
        this.mPathLinePoints = in.createTypedArrayList(LatLng.CREATOR);
        this.mDistance = (Double) in.readValue(Double.class.getClassLoader());
        this.mDuration = (Long) in.readValue(Long.class.getClassLoader());
        this.mStartTime = (Long) in.readValue(Long.class.getClassLoader());
        this.mEndTime = (Long) in.readValue(Long.class.getClassLoader());
        this.mCalorie = (Double) in.readValue(Double.class.getClassLoader());
        this.mSpeed = (Double) in.readValue(Double.class.getClassLoader());
        this.mDistribution = (Double) in.readValue(Double.class.getClassLoader());
        this.mDateTag = in.readString();
    }

    public static final Creator<PathRecord> CREATOR = new Creator<PathRecord>() {
        @Override
        public PathRecord createFromParcel(Parcel in) {
            return new PathRecord(in);
        }

        @Override
        public PathRecord[] newArray(int size) {
            return new PathRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.mStartPoint, flags);
        dest.writeParcelable(this.mEndPoint, flags);
        dest.writeTypedList(this.mPathLinePoints);
        dest.writeValue(this.mDistance);
        dest.writeValue(this.mDuration);
        dest.writeValue(this.mStartTime);
        dest.writeValue(this.mEndTime);
        dest.writeValue(this.mCalorie);
        dest.writeValue(this.mSpeed);
        dest.writeValue(this.mDistribution);
        dest.writeString(this.mDateTag);
    }







}
