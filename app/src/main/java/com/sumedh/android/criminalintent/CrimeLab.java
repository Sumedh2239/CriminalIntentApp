package com.sumedh.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private ArrayList<Crime> mCrimes;
    private static CrimeLab sCrimeLab;
    private static int n  ;
    public static CrimeLab get(Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();
        n=0;
//        for(int i=0;i<100;i++){
//            Crime  crime = new Crime();
//            crime.setmTitle("Crime "+i);
//            crime.setmSolved(i%2==0);
//            mCrimes.add(crime);
//        }

    }

    public void addCrime(Crime c){
        c.setmTitle("Crime"+n);
        n++;
        mCrimes.add(c);

    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime : mCrimes){
            if(crime.getmID().equals(id)){ return crime;}
        }
        return null;
    }



}
