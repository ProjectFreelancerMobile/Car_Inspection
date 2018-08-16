package com.car_inspection.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.car_inspection.R
import com.car_inspection.data.model.StepModifyModel
import kotlinx.android.synthetic.main.adapter_step.view.*


class StepAdapter(var context: Context) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {
    var items: List<StepModifyModel>? = null
    var stepAdapterListener: StepAdapterListener? = null
    var binding = false
    var heightItem = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_step, parent, false)
        val viewTreeObserver = view.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    heightItem = view.measuredHeight
                }
            })
        }
        return StepViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        if (!TextUtils.isEmpty(items?.get(position)?.subStep) && !TextUtils.isEmpty(items?.get(position)?.subStepTitle2))
            holder.tvSubStep.text = items?.get(position)?.subStep + " " + items?.get(position)?.subStepTitle2
        else holder.tvSubStep.visibility = View.GONE
        if (!TextUtils.isEmpty(items?.get(position)?.subStepTitle3))
            holder.tvSubStepTitle.text = items?.get(position)?.subStepTitle3
        binding = true
        if (!TextUtils.isEmpty(items?.get(position)?.rating)) {
            when (items?.get(position)?.rating) {
                "G" -> holder.rgGPF.check(R.id.cbG)
                "P" -> holder.rgGPF.check(R.id.cbP)
                "F" -> holder.rgGPF.check(R.id.cbF)
            }
        } else holder.rgGPF.clearCheck()
        binding = false
        if (!binding)
            holder.rgGPF.setOnCheckedChangeListener { group, checkId ->
                stepAdapterListener?.onRadioGroupCheckChangeListner(group, checkId, position)
            }

        holder.tvNote.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.isNotEmpty())
                    items?.get(position)?.note = p0.toString()
                else items?.get(position)?.note = ""
            }

        })
        holder.tvNote.setOnClickListener { v -> stepAdapterListener?.onTextNoteClickListener(v, position) }
    }

    fun isFinishCheckItem(): Boolean {
        for (item in this.items!!)
            if (TextUtils.isEmpty(item.rating))
                return false
        return true
    }

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubStep = itemView.tvSubStep
        val tvNote = itemView.tvNote
        val rgGPF = itemView.rgGPF
        val tvSubStepTitle = itemView.tvSubStepTitle
    }

    interface StepAdapterListener {
        fun onRadioGroupCheckChangeListner(group: RadioGroup, checkId: Int, position: Int)
        fun onTextNoteClickListener(v: View, position: Int)
    }
}