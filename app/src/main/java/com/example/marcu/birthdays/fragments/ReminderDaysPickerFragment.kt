package com.example.marcu.birthdays.fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import android.widget.NumberPicker
import com.example.marcu.birthdays.R
import kotlinx.android.synthetic.main.alarm_time_picker_fragment_layout.*
import java.lang.ClassCastException


class ReminderDaysPickerFragment : AppCompatDialogFragment() {

    interface DayPickerListener{
        fun applyDays(days: String)
    }

    private lateinit var daysNumberPicker: NumberPicker
    private lateinit var listener: DayPickerListener

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { AlertDialog.Builder(it) }
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.reminder_days_picker_fragment_layout, null)

        builder?.setView(view)?.setTitle("Wie viele Tage möchtest du vorher erinnert werden?")?.setNegativeButton(
            "Abbrechen"
        ) { _: DialogInterface, _: Int ->
        }?.setPositiveButton("Ok") { _, _ ->
            val days = daysNumberPicker.value.toString()
            listener.applyDays(days)
        }

        if (view != null) {
            daysNumberPicker = view.findViewById(com.example.marcu.birthdays.R.id.dayNumberPicker)
            daysNumberPicker.maxValue = 99
            daysNumberPicker.minValue = 1
            daysNumberPicker.wrapSelectorWheel = true
        }

        return builder?.create()!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = context as DayPickerListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement TimePickerListener")
        }
    }
}