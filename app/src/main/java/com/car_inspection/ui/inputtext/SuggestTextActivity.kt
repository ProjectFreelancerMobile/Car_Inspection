package com.car_inspection.ui.inputtext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.car_inspection.R
import kotlinx.android.synthetic.main.suggest_text_activity.*
import android.widget.Toast




class SuggestTextActivity : AppCompatActivity() {
    val LIST_SUGGEST = arrayOf("trầy sơn", "hư lốp", "lớp sơn không nguyên bản", "trầy xước"
            , "có vết lõm nhẹ", "kính vỡ", "bánh xe mềm", "đứt dây diện")
    var position: Int = 0
    var note: String = ""
    lateinit var listTextViewSuggest: ArrayList<TextView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suggest_text_activity)

        position = getIntent().getExtras().getInt("position")
        if (!TextUtils.isEmpty(getIntent().getExtras().getString("note")))
            note = getIntent().getExtras().getString("note")

        initViews()
        addTextSuggest()

    }

    fun initViews() {
        edtNote.requestFocus()
        edtNote.setText(note)
        edtNote.setSelection(edtNote.getText().length)
        edtNote.setHandleDismissingKeyboard { returnResult() }
        edtNote.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                returnResult()
                return@OnEditorActionListener true
            }
            false
        })

        btnExit.setOnClickListener { returnResult() }
    }

    fun returnResult() {
        val returnIntent = Intent()
        returnIntent.putExtra("position", position)
        if (TextUtils.isEmpty(edtNote.text))
            returnIntent.putExtra("note", "")
        else
            returnIntent.putExtra("note", edtNote.text.toString())
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    fun addTextSuggest() {
        for (i in 0..LIST_SUGGEST.size - 1) {
            val textView = TextView(this)
            textView.text = LIST_SUGGEST[i]
            var layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(10, 0, 10, 0)
            textView.layoutParams = layoutParams
            textView.setBackgroundResource(R.drawable.rectangle_boder_radius_orange)
            textView.setPadding(20, 20, 20, 20)

            textView.setOnClickListener {
                edtNote.setText(edtNote.text.toString() + textView.text.toString())
                edtNote.setSelection(edtNote.getText().length)
            }
            flowLayoutSuggest.addView(textView)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ENTER) {
            returnResult()
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        returnResult()
        super.onBackPressed()
    }
}