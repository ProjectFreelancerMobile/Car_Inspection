package com.car_inspection.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.car_inspection.R;
import com.google.android.material.textfield.TextInputEditText;


/**
 * Created by CLICK on 4/5/2018.
 */

public class CustomFontHelper {

    /**
     * Sets a font on a textview based on the custom com.my.package:font attribute
     * If the custom font attribute isn't found in the attributes nothing happens
     * @param textview
     * @param context
     * @param attrs
     */
    public static void setCustomFont(TextView textview, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
        String font = a.getString(R.styleable.CTextViewDefault_customFont);
        setCustomFont(textview, font, context);
        a.recycle();
    }

    public static void setCustomFont(TextView textview, String font, Context context) {
        if(font == null) {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if(tf != null) {
            textview.setTypeface(tf);
        }
    }
    // for button
    public static void setCustomFont(Button button, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
        String font = a.getString(R.styleable.CTextViewDefault_customFont);
        setCustomFont(button, font, context);
        a.recycle();
    }

    public static void setCustomFont(Button button, String font, Context context) {
        if(font == null) {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if(tf != null) {
            button.setTypeface(tf);
        }
    }
    // for Edittext
    public static void setCustomFont(TextInputEditText editText, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CTextViewDefault);
        String font = a.getString(R.styleable.CTextViewDefault_customFont);
        setCustomFont(editText, font, context);
        a.recycle();
    }

    public static void setCustomFont(TextInputEditText editText, String font, Context context) {
        if(font == null) {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if(tf != null) {
            editText.setTypeface(tf);
        }
    }

}