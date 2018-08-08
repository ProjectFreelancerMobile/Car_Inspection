package com.car_inspection.library.commonview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.car_inspection.R;
import com.car_inspection.utils.CustomFontHelper;

import androidx.appcompat.widget.AppCompatButton;


public class CButtonState extends AppCompatButton {
    private static final String TAG = "Button";
    private boolean isActive = false;

    public CButtonState(Context context) {
        super(context);
    }

    public CButtonState(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this,context, attrs);
        setActive(context, attrs);

    }
public boolean isActive(){
        return isActive;
}
    public CButtonState(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this,context, attrs);
        setActive(context, attrs);

    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
        String customFont = a.getString(R.styleable.CTextViewDefault_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

    public void setActive(Context ctx, AttributeSet attrs){
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
        Boolean isActive = a.getBoolean(R.styleable.CTextViewDefault_setActive, true);
    this.isActive = isActive;
        if (isActive){
            setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_orange));
            setTextColor(getResources().getColor(R.color.white_text_color));
        }else {
            setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_disable));
            setTextColor(getResources().getColor(R.color.white_text_disable));

        }
    }

    public void setActive(Boolean isActive){
        this.isActive = isActive;
        if (isActive){
            setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_orange));
            setTextColor(getResources().getColor(R.color.white_text_color));
        }else {
            setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_disable));
            setTextColor(getResources().getColor(R.color.white_text_disable));
        }
    }

}