package com.hfad.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    //ДЗ 301 стр.
    //implements CrimeFragment.OnDeleteCrimeListener {
    //

    static final String EXTRA_CRIME_ID = "com.hfad.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mJumpToFirstButton;
    private Button mJumpToLastButton;
    private int lastIndex;

    //ДЗ 301 стр.
   /* public void onCrimeIdSelected (UUID crimeId) {
        Intent data = new Intent();
        data.putExtra(EXTRA_CRIME_ID, crimeId);
        setResult(Activity.RESULT_OK, data);
        finish(); // CrimePagerActivity
    }*/
    //

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        //ДЗ 243 стр. добавление кнопок для перехода+
        lastIndex = mCrimes.size() - 1;
        mJumpToFirstButton = findViewById(R.id.first_button);
        mJumpToLastButton = findViewById(R.id.last_button);

        mJumpToFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0, true);
                toggleButtons();
            }
        });

        mJumpToLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mViewPager.setCurrentItem(lastIndex, true);
                mViewPager.setCurrentItem(lastIndex, true);
                toggleButtons();
            }
        });

    }

    private void toggleButtons() {
        if (mViewPager.getCurrentItem() == 0) {
            mJumpToFirstButton.setEnabled(false);
        }
        else {
            mJumpToFirstButton.setEnabled(true);
        }

        if (mViewPager.getCurrentItem() == lastIndex) {
            mJumpToLastButton.setEnabled(false);
        }
        else {
            mJumpToLastButton.setEnabled(true);
        }
    }
    //ДЗ 243 стр. добавление кнопок для перехода-
}
