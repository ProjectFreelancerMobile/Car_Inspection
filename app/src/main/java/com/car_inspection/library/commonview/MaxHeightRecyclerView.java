package com.car_inspection.library.commonview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.car_inspection.R;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MaxHeightRecyclerView extends RecyclerView {
    public MaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int padding = 2 * (int) getResources().getDimension(R.dimen.padding_normal);
        int itemHeight = (int) getResources().getDimension(R.dimen.item_suggest_search_height);
        int maxHeightInPixels = 4 * itemHeight + padding;
        heightSpec = View.MeasureSpec.makeMeasureSpec(maxHeightInPixels, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
