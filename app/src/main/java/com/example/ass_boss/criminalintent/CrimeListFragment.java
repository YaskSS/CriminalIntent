package com.example.ass_boss.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final int CRIME_ACTIVITY_REQUEST_CODE = 534;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView crimeRecyclerView;
    private CrimeAdapter adapter;

    private boolean subtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        crimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimeList = crimeLab.getCrimes();
        adapter = new CrimeAdapter(crimeList);
        crimeRecyclerView.setAdapter(adapter);
        updateSubtitle();
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTextView;
        private TextView dateTextView;
        private Crime crime;
        private ImageView solvedImageView;

        public CrimeHolder(View v) {
            super(v);

            itemView.setOnClickListener(this);

            titleTextView = (TextView) itemView.findViewById(R.id.crime_title_textView);
            dateTextView = (TextView) itemView.findViewById(R.id.crime_date_textView);
            solvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved_imageView);
        }

        public void bind(Crime crime) {
            this.crime = crime;
            titleTextView.setText(crime.getTitle());
            dateTextView.setText(DateFormat.format("E MMM dd, yyyy", crime.getDate()));
            solvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE :
                    View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
            startActivityForResult(intent, CRIME_ACTIVITY_REQUEST_CODE);
        }
    }

    private class CrimePoliceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTextView;
        private TextView dateTextView;
        private Button callPoliceButton;

        public CrimePoliceHolder(View v) {
            super(v);

            titleTextView = (TextView) itemView.findViewById(R.id.title_crime_police_textView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_crime_police_textView);
            callPoliceButton = (Button) itemView.findViewById(R.id.requires_policve_crime_police_button);
            callPoliceButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    "CALL POLICE",
                    Toast.LENGTH_SHORT).show();
        }

        public void bind(Crime crime) {
            titleTextView.setText(crime.getTitle());
            dateTextView.setText(crime.getDate().toString());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int CRIME_TYPE = 0;
        private static final int CRIME_POLICE_TYPE = 1;

        private List<Crime> crimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.crimes = crimes;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            int layoutId = 0;
            if (viewType == CRIME_TYPE) {
                layoutId = R.layout.item_list_crime;
                return new CrimeHolder(inflater.inflate(layoutId, parent, false));
            } else if (viewType == CRIME_POLICE_TYPE) {
                layoutId = R.layout.item_list_crime_police;
                return new CrimePoliceHolder(inflater.inflate(layoutId, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Crime crime = crimes.get(holder.getAdapterPosition());
            if (crime.isRequiresPolice()) {
                ((CrimePoliceHolder) holder).bind(crime);
            } else {
                ((CrimeHolder) holder).bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (crimes.get(position).isRequiresPolice()) {
                return CRIME_POLICE_TYPE;
            } else {
                return CRIME_TYPE;
            }
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subTitle = getString(R.string.subtitle_format, crimeCount);

        if (!subtitleVisible) {
            subTitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

        appCompatActivity.getSupportActionBar().setSubtitle(subTitle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem item = menu.findItem(R.id.show_subtitle);
        if (subtitleVisible) {
            item.setTitle(R.string.hide_subtitle);
        } else {
            item.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);

                Intent intent = CrimePagerActivity
                        .newIntent(getActivity(), crime.getId());

                startActivityForResult(intent, CRIME_ACTIVITY_REQUEST_CODE);

                return true;
            case R.id.show_subtitle:

                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();

                updateSubtitle();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CRIME_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            //adapter.notifyItemChanged(data.getIntExtra(CrimePagerActivity.EXTRA_CRIME_POSITION, 0));
            adapter.notifyDataSetChanged();
        }
    }
}
