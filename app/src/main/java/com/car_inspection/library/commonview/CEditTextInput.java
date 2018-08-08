package com.car_inspection.library.commonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.car_inspection.R;
import com.car_inspection.utils.CustomFontHelper;
import com.google.android.material.textfield.TextInputEditText;


public class CEditTextInput extends TextInputEditText {
    private static final String TAG = "TextView";
    private Context mContext;
    public Runnable runnableBackPress;

    public CEditTextInput(Context context) {
        super(context);
        this.mContext = context;
    }

    public CEditTextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this,context, attrs);
        setTextStyleWhite(context, attrs);
        this.mContext = context;

    }

    public CEditTextInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this,context, attrs);
        setTextStyleWhite(context, attrs);
        this.mContext = context;

    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
        String customFont = a.getString(R.styleable.CTextViewDefault_customFont);

//        setTextColor(getResources().getColor(R.color.black_text_color));
//        setHintTextColor(getResources().getColor(R.color.black_text_color_hint));
        setCustomFont(ctx, customFont);
        a.recycle();
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

//    @Override
//    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        if (runnableBackPress != null) {
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                // Do your thing.
//                runnableBackPress.run();
//                return true;  // So it is not propagated.
//            }
//        }
//        return super.dispatchKeyEvent(event);
//    }


    @Override
    public void setError(CharSequence error) {
        Drawable icon = getResources().getDrawable(R.drawable.ic_error);
        if (error != null){
            icon.setBounds(new Rect(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight()));

        }

        setCompoundDrawables(null, null, icon, null);
    }

}
