package com.expansion.lg.kimaru.expansion.other

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by kimaru on 4/27/17.
 * Adapted from Google
 */

class DashboardLayout : ViewGroup {

    private var mMaxChildWidth = 0
    private var mMaxChildHeight = 0

    constructor(context: Context) : super(context, null) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mMaxChildWidth = 0
        mMaxChildHeight = 0

        // Measure once to find the maximum child size.

        var childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.AT_MOST)
        var childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.AT_MOST)

        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)

            mMaxChildWidth = Math.max(mMaxChildWidth, child.measuredWidth)
            mMaxChildHeight = Math.max(mMaxChildHeight, child.measuredHeight)
        }

        // Measure again for each child to be exactly the same size.

        childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                mMaxChildWidth, View.MeasureSpec.EXACTLY)
        childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                mMaxChildHeight, View.MeasureSpec.EXACTLY)

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }

        setMeasuredDimension(
                View.resolveSize(mMaxChildWidth, widthMeasureSpec),
                View.resolveSize(mMaxChildHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var width = r - l
        var height = b - t

        val count = childCount

        // Calculate the number of visible children.
        var visibleCount = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            ++visibleCount
        }

        if (visibleCount == 0) {
            return
        }

        // Calculate what number of rows and columns will optimize for even horizontal and
        // vertical whitespace between items. Start with a 1 x N grid, then try 2 x N, and so on.
        var bestSpaceDifference = Integer.MAX_VALUE
        var spaceDifference: Int

        // Horizontal and vertical space between items
        var hSpace = 0
        var vSpace = 0

        var cols = 1
        var rows: Int

        while (true) {
            rows = (visibleCount - 1) / cols + 1

            hSpace = (width - mMaxChildWidth * cols) / (cols + 1)
            vSpace = (height - mMaxChildHeight * rows) / (rows + 1)

            spaceDifference = Math.abs(vSpace - hSpace)
            if (rows * cols != visibleCount) {
                spaceDifference *= UNEVEN_GRID_PENALTY_MULTIPLIER
            }

            if (spaceDifference < bestSpaceDifference) {
                // Found a better whitespace squareness/ratio
                bestSpaceDifference = spaceDifference

                // If we found a better whitespace squareness and there's only 1 row, this is
                // the best we can do.
                if (rows == 1) {
                    break
                }
            } else {
                // This is a worse whitespace ratio, use the previous value of cols and exit.
                --cols
                rows = (visibleCount - 1) / cols + 1
                hSpace = (width - mMaxChildWidth * cols) / (cols + 1)
                vSpace = (height - mMaxChildHeight * rows) / (rows + 1)
                break
            }

            ++cols
        }

        // Lay out children based on calculated best-fit number of rows and cols.

        // If we chose a layout that has negative horizontal or vertical space, force it to zero.
        hSpace = Math.max(0, hSpace)
        vSpace = Math.max(0, vSpace)

        // Re-use width/height variables to be child width/height.
        width = (width - hSpace * (cols + 1)) / cols
        height = (height - vSpace * (rows + 1)) / rows

        var left: Int
        var top: Int
        var col: Int
        var row: Int
        var visibleIndex = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }

            row = visibleIndex / cols
            col = visibleIndex % cols

            left = hSpace * (col + 1) + width * col
            top = vSpace * (row + 1) + height * row

            child.layout(left, top,
                    if (hSpace == 0 && col == cols - 1) r else left + width,
                    if (vSpace == 0 && row == rows - 1) b else top + height)
            ++visibleIndex
        }
    }

    companion object {
        private val UNEVEN_GRID_PENALTY_MULTIPLIER = 10
    }
}
