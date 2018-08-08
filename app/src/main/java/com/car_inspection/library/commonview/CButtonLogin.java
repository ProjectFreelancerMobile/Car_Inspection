package com.car_inspection.library.commonview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.car_inspection.R;
import com.car_inspection.utils.CustomFontHelper;

import androidx.appcompat.widget.AppCompatButton;


public class CButtonLogin extends AppCompatButton {
    private static final String TAG = "Button";

    public CButtonLogin(Context context) {
        super(context);
    }

    public CButtonLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this,context, attrs);
        setActive(context, attrs);

    }

    public CButtonLogin(Context context, AttributeSet attrs, int defStyle) {
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

    public void setActive(Context ctx, AttributeSet attrs) {
        try {
            TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
            Boolean isActive = a.getBoolean(R.styleable.CTextViewDefault_setActive, true);

            if (isActive) {
                setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_orange));
                setTextColor(getResources().getColor(R.color.white_text_color));
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_white_24dp, 0);
            } else {
                setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_disable));
                setTextColor(getResources().getColor(R.color.white_text_disable));
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_white_24dp_disable, 0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setActive(Boolean isActive) {
        try {
            if (isActive) {
                setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_orange));
                setTextColor(getResources().getColor(R.color.white_text_color));
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_white_24dp, 0);
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_white_24dp, 0);
                }

            } else {
                setBackground(getResources().getDrawable(R.drawable.background_rectangle_radius_disable));
                setTextColor(getResources().getColor(R.color.white_text_disable));
                setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_white_24dp_disable, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
