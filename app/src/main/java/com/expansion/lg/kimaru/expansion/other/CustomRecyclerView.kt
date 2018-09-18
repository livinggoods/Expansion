package com.expansion.lg.kimaru.expansion.other

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.widget.ListView

/**
 * Created by kimaru on 5/15/17.
 */


/**
 * Created by kimaru on 5/12/17.
 */

class CustomRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private var params: android.view.ViewGroup.LayoutParams? = null
    private var prevCount = 0

    override fun onDraw(canvas: Canvas) {
        var count = 0
        if (this.adapter != null) {
            count = this.adapter.itemCount
        }
        if (count != prevCount) {
            val height = getChildAt(0).height + 1
            prevCount = count
            params = layoutParams
            params!!.height = count * height
            layoutParams = params
        }

        super.onDraw(canvas)
    }

}
