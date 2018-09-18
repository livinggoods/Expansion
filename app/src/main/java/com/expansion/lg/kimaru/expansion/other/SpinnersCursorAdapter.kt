package com.expansion.lg.kimaru.expansion.other

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

import com.expansion.lg.kimaru.expansion.R

/**
 * Created by kimaru on 3/23/17.
 */

class SpinnersCursorAdapter(context: Context, cursor: Cursor) : CursorAdapter(context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.cursor_adapter, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val shownText = view.findViewById<View>(R.id.shownText) as TextView
        val idTracker = view.findViewById<View>(R.id.idTracker) as TextView

        val textToShow = cursor.getString(cursor.getColumnIndex("name"))
        val idToShow = cursor.getInt(0)

        shownText.text = textToShow
        idTracker.text = idToShow.toString()
        idTracker.visibility = View.INVISIBLE
    }
}
