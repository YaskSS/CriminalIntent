package com.example.ass_boss.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView crimeRecyclerView;
    private CrimeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        crimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        adapter = new CrimeAdapter(crimeLab.getCrimes());
        crimeRecyclerView.setAdapter(adapter);
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
            Intent intent = CrimeActivity.newIntent(getActivity(), crime.getId());
            startActivity(intent);
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
}
