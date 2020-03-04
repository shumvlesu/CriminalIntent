package com.hfad.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    //ДЗ Текст при пустом списке 282 стр.
    private View mTextView;
    //--
    private CrimeAdapter mAdapter;
    private int mLastUpdatedPosition = -1;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //ДЗ Текст при пустом списке 282 стр.
        //пришлость испоьзовать лиеар лайоут в fragment_crime_list. Иначе программа вылетала при запуске.
        //Видимо плохо работают элементы тексвью в сочетании с AppCombat компонентами на одной активити.
        mTextView = view.findViewById(R.id.no_crime_there);
        //--

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        //ДЗ Текст при пустом списке 282 стр.
        if(CrimeLab.get(getActivity()).getCrimes().size() <= 0) {
            mTextView.setVisibility(View.VISIBLE);
        } else {
            updateUI();
        }

        //updateUI();
        //--

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    // реализация меню гл. 13 у фрагмента++
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        //стр.278
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }

    }

    //стр 274
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //--

    private void updateSubtitle() {
        //стр.276
        //получаем активность
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        //считаем количество записей в ресайклвью
        int crimeCount = crimeLab.getCrimes().size();
        //в строку ресурса впихиваем полученное количество
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        //скрыть или показать субтитры
        if (!mSubtitleVisible) {
            subtitle = null;
        }


        //Затем  активность,  являющаяся  хостом  для  CrimeListFragment,  преобразуется в AppCompatActivity.
        //так как мы используем AppCompat компоненту.
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        //Заносим в заголовок активности нашу строку
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    private void updateUI() {
        //CrimeLab crimeLab = CrimeLab.get(getActivity());
        //List<Crime> crimes = crimeLab.getCrimes();
        //можно и так
        List<Crime> crimes = CrimeLab.get(getActivity()).getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            //mAdapter.notifyDataSetChanged();
            //упр 1 на 232
            if (mLastUpdatedPosition > -1) {
                mAdapter.setCrimes(crimes);
                mAdapter.notifyItemChanged(mLastUpdatedPosition);
                mLastUpdatedPosition = -1;
            } else {

                mAdapter.setCrimes(crimes);
                //стр. 274-275 при кнопке buck созданное преступление не появляется в списке а при up появляется. Что за втф?
                //upd Разобрался :) notifyDataSetChanged() не всегда корректно работает. Как решение создал setData() Теперь отрабатывает по всем кнопкам.
                //mAdapter.notifyDataSetChanged();
                mAdapter.setData(crimes);
            }
            updateSubtitle();
        }

    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());

            //mDateTextView.setText(mCrime.getDate().toString());
            String formatedDate = new SimpleDateFormat("EEE, MMM, dd.MM.yyyy", Locale.getDefault()).format(mCrime.getDate());

            String timeFormat = new SimpleDateFormat(CrimeFragment.TIME_FORMAT,Locale.getDefault()).format(mCrime.getTime());


            mDateTextView.setText(formatedDate+", "+getString(R.string.time)+" "+timeFormat);

            //если презтупление расклыто то выводим наручники
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }


        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getActivity(),CrimeActivity.class);

            //CrimeActivity заменили на CrimePagerActivity
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());

            //упр 1 на 232
            mLastUpdatedPosition = this.getAdapterPosition();
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;


        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }


        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);

        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void setData(List<Crime> list) {
            this.mCrimes = list;
            notifyDataSetChanged();
        }


    }


}
