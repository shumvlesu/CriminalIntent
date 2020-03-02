package com.hfad.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;
    private Map<UUID,Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context) {
        //mCrimes = new ArrayList<>();
        mCrimes = new LinkedHashMap<>();

        /*  for (int i=0; i<100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i % 2 == 0);
            //mCrimes.add(crime);
            mCrimes.put(crime.getId(), crime);
        }*/

    }

    public void addCrime(Crime c) {
        //mCrimes.add(c);
        //Из за дз другой код немного использую чем на стр.273
        mCrimes.put(c.getId(),c);
    }


    public List<Crime> getCrimes() {
        //return mCrimes;
        return new ArrayList<>(mCrimes.values());
    }

    public Crime getCrime(UUID id) {
       // for (Crime crime : mCrimes) {
       //     if (crime.getId().equals(id)) {
       //         return crime;
       //     }
       // }
       // return null;
       return mCrimes.get(id);
    }

    //ДЗ Удаление преступления 281стр++
    public void deleteItem(UUID crimeID) {
        mCrimes.remove(crimeID);
    }
    //--

}
