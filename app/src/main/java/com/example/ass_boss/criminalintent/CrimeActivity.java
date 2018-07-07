package com.example.ass_boss.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID =
            "com.example.ass_boss.criminalintent.crime_id";
    public static final String EXTRA_CRIME_POSITION =
            "com.example.ass_boss.criminalintent.crime_position";

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        return CrimeFragment.getInstance(crimeId);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CRIME_POSITION, getIntent().getIntExtra(EXTRA_CRIME_POSITION, 0));
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    public static Intent newIntent(Context packageContext, UUID crimeId, int crimePosition) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_POSITION, crimePosition);
        return intent;
    }
}
