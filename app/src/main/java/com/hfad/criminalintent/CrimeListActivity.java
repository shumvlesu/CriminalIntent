package com.hfad.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    //Стр. 335  Переход к файлу двухпанельного макета.
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }


}
