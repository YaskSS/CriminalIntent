package com.example.ass_boss.criminalintent;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

public class Crime implements Comparable<Crime> {

    private UUID id;
    private String title;
    private Date date;
    private boolean solved;
    private boolean requiresPolice;

    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isRequiresPolice() {
        return requiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        this.requiresPolice = requiresPolice;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Crime)) return false;

        Crime crime = (Crime) obj;

        return crime.id == getId();
    }

    @Override
    public int compareTo(@NonNull Crime o) {
        return 0;
    }
}
