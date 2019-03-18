package com.example.marcu.birthdays.gui

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.widget.EditText
import android.widget.TimePicker
import java.util.*
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.core.hourReminder
import com.example.marcu.birthdays.core.minuteReminder


class AlarmTimePicker: DialogFragment(), TimePickerDialog.OnTimeSetListener{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val atp = TimePickerDialog(
            activity, R.style.Theme_Design_Light_NoActionBar, this, hour, minute, true
        )

        atp.setTitle(resources.getString(R.string.time_picker_headline))

        return atp
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val reminderTimeText = activity?.findViewById(R.id.reminderTimeText) as EditText
        val minuteString = if (minute.toString().length == 1){
            "0" + Integer.toString(minute)
        } else {
            Integer.toString(minute)
        }
        val hourString = if (hourOfDay.toString().length == 1){
            "0" + Integer.toString(hourOfDay)
        } else {
            Integer.toString(hourOfDay)
        }
        reminderTimeText.text = Editable.Factory.getInstance().newEditable("$hourString : $minuteString")

        minuteReminder = minute
        hourReminder = hourOfDay

    }

}