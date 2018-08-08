package com.car_inspection.library.commonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.car_inspection.R;
import com.car_inspection.utils.CustomFontHelper;

import androidx.appcompat.widget.AppCompatTextView;


public class CTextViewDefault extends AppCompatTextView {
    private static final String TAG = "TextView";

    public CTextViewDefault(Context context) {
        super(context);
    }

    public CTextViewDefault(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this,context, attrs);
        setTextStyleWhite(context, attrs);
        //setCustomTextColor(context, attrs);
    }

    public CTextViewDefault(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this,context, attrs);
        setTextStyleWhite(context, attrs);
        //setCustomTextColor(context, attrs);
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
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

    private void setTextStyleWhite(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
        boolean customText = a.getBoolean(R.styleable.CTextViewDefault_textWhite, false);

        if (customText){
            setTextColor(getResources().getColor(R.color.white_text_color));
            setHintTextColor(getResources().getColor(R.color.white_text_color_hint));

        }else {
            setTextColor(getResources().getColor(R.color.black_text_color));
            setHintTextColor(getResources().getColor(R.color.black_text_color_hint));
        }
        a.recycle();
    }

//    private void setCustomTextColor(Context ctx, AttributeSet attrs) {
//        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CTextView);
//        String customText = a.getString(R.styleable.CTextView_textColorCustom);
//
//        setTextColor(a.getColor(R.styleable.CTextView_textColorCustom, 0));
//
//        a.recycle();
//    }

}
