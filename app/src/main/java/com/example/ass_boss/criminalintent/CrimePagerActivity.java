package com.example.ass_boss.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID =
            "com.example.ass_boss.criminalintent.crime_id";

    public static final String EXTRA_CRIME_POSITION =
            "com.example.ass_boss.criminalintent.crime_position";

    private ViewPager crimeViewPager;
    private List<Crime> crimes;
    private UUID crimeId;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        crimeViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        crimes = CrimeLab.getInstance(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();

        crimeViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = crimes.get(position);
                return CrimeFragment.getInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });

        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId)) {
                crimeViewPager.setCurrentItem(i);
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CRIME_POSITION, crimeViewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
