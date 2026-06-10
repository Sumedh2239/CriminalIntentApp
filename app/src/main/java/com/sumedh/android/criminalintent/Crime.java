package com.sumedh.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;


    public Crime(){
        this(UUID.randomUUID());
//        mID = UUID.randomUUID();
//        mDate = new Date();
    }
    public Crime(UUID id){
        mID = id;
        mDate = new Date();
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public UUID getmID() {
        return mID;
    }

    public void setmID(UUID mID) {
        this.mID = mID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
    public String getmSuspect(){
        return mSuspect;
    }
    public void setmSuspect(String suspect){
        mSuspect=suspect;
    }
}
