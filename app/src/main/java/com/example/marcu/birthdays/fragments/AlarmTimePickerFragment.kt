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
import java.lang.ClassCastException
import java.util.*


class AlarmTimePickerFragment : AppCompatDialogFragment() {

    interface TimePickerListener{
        fun applyTime(time: String)
    }

    private lateinit var hourNumberPicker: NumberPicker
    private lateinit var minuteNumberPicker: NumberPicker
    private lateinit var listener: TimePickerListener

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { AlertDialog.Builder(it) }
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.alarm_time_picker_fragment_layout, null)

        builder?.setView(view)?.setTitle("Wähle eine Zeit für deinen Alarm")?.setNegativeButton(
            "Abbrechen"
        ) { _: DialogInterface, _: Int ->
        }?.setPositiveButton("Ok") { _, _ ->
            //TODO Sollte immer zweistellig sein
            val time = """${hourNumberPicker.value} : ${minuteNumberPicker.value}"""
            listener.applyTime(time)
        }

        //TODO Sollte immer zweistellig sein
        if (view != null) {
            hourNumberPicker = view.findViewById(com.example.marcu.birthdays.R.id.hourNumberPicker)
            hourNumberPicker.maxValue = 23
            hourNumberPicker.minValue = 0
            hourNumberPicker.wrapSelectorWheel = true
            hourNumberPicker.setFormatter(MyTwoDigitFormatter())
        }

        if (view != null) {
            minuteNumberPicker = view.findViewById(com.example.marcu.birthdays.R.id.minuteNumberPicker)
            minuteNumberPicker.maxValue = 59
            minuteNumberPicker.minValue = 0
            minuteNumberPicker.wrapSelectorWheel = true
            minuteNumberPicker.setFormatter(MyTwoDigitFormatter())
        }

        return builder?.create()!!
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listener = context as TimePickerListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement TimePickerListener")
        }
    }

    class MyTwoDigitFormatter:NumberPicker.Formatter {
        override fun format(value: Int): String {
            return String.format("%02d", value)
        }
    }
}