package com.sumedh.android.criminalintent;

import static android.widget.Toast.LENGTH_LONG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button deleteButton;
    private static final String ARG_CRIME_ID = "crime_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    public static CrimeFragment newInstance(UUID crimeID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeID);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCrime = new Crime();
//        UUID crimeID = (UUID) getActivity().getIntent().getSerializableExtra(ARG_CRIME_ID);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
        //Datepickerfragment datefragment=newInstance()
    }

    @Override
    public void onPause(){
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime,group,false);

        mTitleField= (EditText) v.findViewById(R.id.crime_title);

        mTitleField.setText(mCrime.getmTitle());

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }
        });
        mDateButton = (Button)v.findViewById(R.id.crime_date);
//        mDateButton.setText(mCrime.getmDate().toString());
//        mDateButton.setEnabled(false);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment d = DatePickerFragment.newInstance(mCrime.getmDate());
                //newInstance(mCrime.getmDate)
                d.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                d.show(manager,DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        deleteButton =(Button) v.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = CrimeLab.get(getActivity()).deleteCrime(mCrime);
                Toast.makeText(getActivity(),"Deleted Successfully",LENGTH_LONG);

            }
        });
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!=Activity.RESULT_OK){ return; }
        if(requestCode==REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
               }
    }
    private void updateDate(){
        mDateButton.setText(mCrime.getmDate().toString());

    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK,null);
    }


}
