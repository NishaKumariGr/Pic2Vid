package edu.dartmouth.nishacs.cs65project.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nisha on 3/3/16.
 * This function takes all the parameters required to
 * generate the query and the generated the query accordingly.
 */
public class Query {
    //Initialising the variable witht he default values
    public Integer mRadius;
    public String mDuration;
    public Long mStartDate, mEnddate;
    public Boolean mIsSceneryChecked = false, mIsPeopleChecked = false, mIsSelfieClicked = false;
    public LatLng mLocation;

    public LatLng getmLocation() {
        return mLocation;
    }

    public void setmLocation(LatLng mLocation) {
        this.mLocation = mLocation;
    }

    public Integer getmRadius() {
        return mRadius;
    }

    public void setmRadius(Integer mRadius) {
        this.mRadius = mRadius;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public Long getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(Long mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Long getmEnddate() {
        return mEnddate;
    }

    public void setmEnddate(Long mEnddate) {
        this.mEnddate = mEnddate;
    }

    public Boolean getmIsSceneryChecked() {
        return mIsSceneryChecked;
    }

    public void setmIsSceneryChecked(Boolean mIsSceneryChecked) {
        this.mIsSceneryChecked = mIsSceneryChecked;
    }

    public Boolean getmIsPeopleChecked() {
        return mIsPeopleChecked;
    }

    public void setmIsPeopleChecked(Boolean mIsPeopleChecked) {
        this.mIsPeopleChecked = mIsPeopleChecked;
    }

    public Boolean getmIsSelfieClicked() {
        return mIsSelfieClicked;
    }

    public void setmIsSelfieClicked(Boolean mIsSelfieClicked) {
        this.mIsSelfieClicked = mIsSelfieClicked;
    }
}
