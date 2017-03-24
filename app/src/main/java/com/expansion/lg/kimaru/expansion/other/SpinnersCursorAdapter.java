package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.expansion.lg.kimaru.expansion.R;

/**
 * Created by kimaru on 3/23/17.
 */

public class SpinnersCursorAdapter extends CursorAdapter {
    public SpinnersCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.cursor_adapter, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView shownText = (TextView) view.findViewById(R.id.shownText);
        TextView idTracker = (TextView) view.findViewById(R.id.idTracker);

        String textToShow = cursor.getString(cursor.getColumnIndex("name"));
        int idToShow = cursor.getInt(0);

        shownText.setText(textToShow);
        idTracker.setText(String.valueOf(idToShow));
    }
}
