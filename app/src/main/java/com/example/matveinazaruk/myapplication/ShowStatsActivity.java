package com.example.matveinazaruk.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * Created by crocodilys on 11.5.15.
 */
public class ShowStatsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResultsDB db = new ResultsDB(this);
        setListAdapter(new ResultsAdapter(this, db.getResults()));
    }
}
