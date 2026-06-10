package com.sumedh.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeDbSchema.CrimeBaseHelper;
import database.CrimeDbSchema.CrimeCursorWrapper;
import database.CrimeDbSchema.CrimeDbSchema;

public class CrimeLab {

    private ArrayList<Crime> mCrimes;
    private static CrimeLab sCrimeLab;
    private static int n;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
//        mCrimes = new ArrayList<>();
//        n = 0;
//        for(int i=0;i<100;i++){
//            Crime  crime = new Crime();
//            crime.setmTitle("Crime "+i);
//            crime.setmSolved(i%2==0);
//            mCrimes.add(crime);
//        }

    }

    public void addCrime(Crime c) {
//        c.setmTitle("Crime" + n);
//        n++;
//        mCrimes.add(c);
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }

    /**
     *
     * Delete the crime based on ID return no of rows affected 0 otherwise
     * @param crime
     * @return
     */
    public int deleteCrime(Crime crime){
        String uuidString = crime.getmID().toString();
        ContentValues values = getContentValues(crime);
        return mDatabase.delete(CrimeDbSchema.CrimeTable.NAME,CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }
    public List<Crime> getCrimes() {
//        return mCrimes;
//        return new ArrayList<>();
        List<Crime> crimes = new ArrayList<>();


//        cursorWrapper.getCrime();
        try (CrimeCursorWrapper cursor = queryCrimes(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        return crimes;
    }


    public Crime getCrime(UUID id) {
//        for (Crime crime : mCrimes) {
//            if (crime.getmID().equals(id)) {
//                return crime;
//            }
//        }

        try( CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ", new String[]{id.toString()})){
            if (cursorWrapper.getCount() == 0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        }
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getmID().toString();
        ContentValues contentValues = getContentValues(crime);
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, contentValues, CrimeDbSchema.CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});

    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getmID().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getmDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.ismSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getmSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }

}
