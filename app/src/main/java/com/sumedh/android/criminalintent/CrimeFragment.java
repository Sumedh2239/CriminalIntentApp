package com.sumedh.android.criminalintent;

import static android.widget.Toast.LENGTH_LONG;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButtonToggleGroup;

//import java.text.DateFormat;
import java.io.File;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    //View Objects
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button deleteButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;


    //Constants
    private static final String ARG_CRIME_ID = "crime_id";

    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT =1;
    private static final int REQUEST_PHOTO =2;
    public static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);

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
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        //Datepickerfragment datefragment=newInstance()
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, group, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);

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
        mDateButton = (Button) v.findViewById(R.id.crime_date);
//        mDateButton.setText(mCrime.getmDate().toString());
//        mDateButton.setEnabled(false);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment d = DatePickerFragment.newInstance(mCrime.getmDate());
                //newInstance(mCrime.getmDate)
                d.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                d.show(manager, DIALOG_DATE);
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
        deleteButton = (Button) v.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = CrimeLab.get(getActivity()).deleteCrime(mCrime);
                Toast.makeText(getActivity(), "Deleted Successfully", LENGTH_LONG);

            }
        });
        mReportButton = v.findViewById(R.id.report_button);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i = Intent.createChooser(i,getString(R.string.send_report));

                startActivity(i);
            }
        });
        final Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        pickContactIntent.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton = v.findViewById(R.id.choose_suspect_button);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivityForResult(pickContactIntent,REQUEST_CONTACT);

            }

        });
        if(mCrime.getmSuspect() !=  null){
            mSuspectButton.setText(mCrime.getmSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContactIntent,PackageManager.MATCH_DEFAULT_ONLY)==null){
//            mSuspectButton.setEnabled(false);
        }
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null & captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto){
//            Uri uri = Uri.fromFile(mPhotoFile);
            Uri uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.sumedh.android.criminalintent.fileprovider",
                    mPhotoFile
            );

            captureImage.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);

        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
        }
        if(requestCode == REQUEST_CONTACT && data!=null){
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);

            try {
                if(c.getCount() == 0){
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setmSuspect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                c.close();
            }
        }
        if(requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }

    }

    private void updateDate() {
        mDateButton.setText(mCrime.getmDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if(mCrime.ismSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEEE , MMM dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getmDate()).toString();

        String suspect = mCrime.getmSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getmTitle(), dateString, solvedString, suspect);
        return report;
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    public void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }


}
