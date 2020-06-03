package com.hfad.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hfad.criminalintent.database.CrimeBaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.hfad.criminalintent.database.CrimeCursorWrapper;
import com.hfad.criminalintent.database.CrimeDbSchema.CrimeTable;

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;
    //private Map<UUID,Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context) {
        //mCrimes = new ArrayList<>();
        mContext = context.getApplicationContext();

        /*При вызове getWritableDatabase() класс CrimeBaseHelper выполняет следующее:
        1) Открывает  /data/data/com. .android.criminalintent/databases/crimeBase.db. Если файл базы данных не существует, то он создается;
        2) Если  база  данных  открывается  впервые,  вызывает  метод  onCreate(SQLite­ Database) с последующим сохранением последнего номера версии;
        3) Если  база  данных  открывается  не  впервые,  проверяет  номер  ее  версии. Если  версия  базы  данных  в  CrimeOpenHelper  выше,  то  вызывается  метод  onUpgrade(SQLiteDatabase, int, int).*/
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        //mCrimes = new LinkedHashMap<>();

    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        //добавление записи в базу данных
        mDatabase.insert(CrimeTable.NAME, null, values);

        //mCrimes.add(c);
        //Из за дз другой код немного использую чем на стр.273
        //mCrimes.put(c.getId(),c);
    }


    public List<Crime> getCrimes() {
        //return mCrimes;
        //return new ArrayList<>(mCrimes.values());

        //return new ArrayList<>();

        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;



    }

    public Crime getCrime(UUID id) {

       //return mCrimes.get(id);
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    //Определение местонахождения файла фотографии стр.323
    public File getPhotoFile(Crime crime) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
        if (externalFilesDir == null) {
        }



        //Обновление записи бд
    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values,CrimeTable.Cols.UUID + " = ?", new String[] { uuidString });
    }

    //Запрос для получения данных Crime из БД
    //private Cursor queryCrimes(String whereClause, String[] whereArgs) {
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // columns - с null выбираются все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        //return cursor;
        return new CrimeCursorWrapper(cursor);
    }




    //ДЗ Удаление преступления 281стр++
    //public void deleteItem(UUID crimeID) {
    //    mCrimes.remove(crimeID);
    //}
    //--

    //ДЗ Удаление преступления 301стр++
    public void deleteItem(UUID crimeID) {

       String uuidString = crimeID.toString();

        try {
            mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[] { uuidString });
        } finally {

        }

    }

    // Преобразовывает объект Crime в ContentValues
    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        //ДЗ
        values.put(CrimeTable.Cols.TIME, crime.getTime().getTime());
        //
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());


        return values;
    }


    }
