package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by kimaru on 5/15/17.
 */



/**
 * Created by kimaru on 5/12/17.
 */

public class CustomRecyclerView extends RecyclerView {

    private android.view.ViewGroup.LayoutParams params;
    private int prevCount = 0;

    public CustomRecyclerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        int count = 0;
        if(this.getAdapter() != null){
            count = this.getAdapter().getItemCount();
        }
        if (count != prevCount)
        {
            int height = getChildAt(0).getHeight() + 1 ;
            prevCount = count;
            params = getLayoutParams();
            params.height =count * height;
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }

}
