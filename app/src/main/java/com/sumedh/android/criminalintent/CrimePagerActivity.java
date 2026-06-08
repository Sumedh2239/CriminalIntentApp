package com.sumedh.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_ID="com.sumedh.android.criminalintent.crimeID";

    public static Intent newIntent(Context context, UUID crimeID){
        Intent i = new Intent(context,CrimePagerActivity.class);
        i.putExtra(EXTRA_CRIME_ID,crimeID);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        viewPager =(ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmID());

            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for(int i =0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getmID().equals(crimeID)){
                viewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
