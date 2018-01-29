package com.example.jdgjapp.Util;

import android.widget.GridView;

/**
 * Created by mwh on 2018/1/29.
 */

public class GridViewForScrollView extends GridView
{
    public GridViewForScrollView(android.content.Context context,
                                 android.util.AttributeSet attrs)
    {
        super(context, attrs);
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
