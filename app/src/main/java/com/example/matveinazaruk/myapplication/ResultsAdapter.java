package com.example.matveinazaruk.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
* Created by crocodilys on 11.5.15.
*/
class ResultsAdapter extends ResourceCursorAdapter {

    public ResultsAdapter(Context context, Cursor c) {
        super(context, R.layout.row_view, c, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(cursor.getString(cursor.getColumnIndex(ResultsDB.KEY_NAME)));

        TextView result = (TextView) view.findViewById(R.id.result);
        result.setText(cursor.getString(cursor.getColumnIndex(ResultsDB.KEY_RESULT)));
    }
}
