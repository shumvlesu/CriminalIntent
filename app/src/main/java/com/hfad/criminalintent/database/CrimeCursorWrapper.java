package com.hfad.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.hfad.criminalintent.Crime;
import com.hfad.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;


//CursorWrapper — этот класс позволяет дополнить класс Cursor
//стр. 295
public class CrimeCursorWrapper  extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        //ДЗ
        long time = getLong(getColumnIndex(CrimeTable.Cols.TIME));
        //
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        //ДЗ
        crime.setTime(new Date(time));
        //
        crime.setSolved(isSolved != 0);
        return crime;

    }

}
