package com.example.marcu.birthdays.gui.dialoges

import android.content.Intent
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.util.Log
import android.widget.EditText
import com.example.marcu.birthdays.birthdays.BirthdaysDBHandler
import com.example.marcu.birthdays.birthdays.Birthday
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SaveEditBirthday : DialogFragment() {

    private lateinit var fnameText: EditText
    private lateinit var snameText: EditText
    private lateinit var dayText: EditText
    private lateinit var monthText: EditText
    private lateinit var yearText: EditText

    private lateinit var dbHandler: BirthdaysDBHandler

    private var isEdit = false

    private fun getBirthday(): Birthday? {
        val fname = fnameText.text.toString()
        val sname = snameText.text.toString()
        var day = dayText.text.toString()
        var month = monthText.text.toString()
        val year = yearText.text.toString()

        if (!checkNameValidity(fname, sname)) {
            return null
        }

        if (!checkBirthdayValidity(day, month, year)) {
            return null
        }

        if (day.length == 1) {
            day = "0$day"
        }

        if (month.length == 1) {
            month = "0$month"
        }

        val birthday = "$day.$month.$year"

        return Birthday(
            fname,
            sname,
            birthday
        )
    }

    //sets the texts in the text fields, if editing an entry
    private fun setTexts(intent: Intent) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!
        val birthday = LocalDate.parse(intent.getStringExtra("birthday"), formatter)

        fnameText.text =
            Editable.Factory.getInstance().newEditable(intent.getStringExtra("first name"))
        snameText.text =
            Editable.Factory.getInstance().newEditable(intent.getStringExtra("second name"))
        if (birthday.dayOfMonth.toString().length == 1) {
            dayText.text =
                Editable.Factory.getInstance().newEditable("0" + birthday.dayOfMonth.toString())
        } else {
            dayText.text =
                Editable.Factory.getInstance().newEditable(birthday.dayOfMonth.toString())
        }
        if (birthday.monthValue.toString().length == 1) {
            monthText.text =
                Editable.Factory.getInstance().newEditable("0" + birthday.monthValue.toString())
        } else {
            monthText.text =
                Editable.Factory.getInstance().newEditable(birthday.monthValue.toString())
        }
        yearText.text = Editable.Factory.getInstance().newEditable(birthday.year.toString())
    }


    /*
    checks the validity of the entries in first name and second name
    it's not valid, if the entry is empty or not just letters
     */
    private fun checkNameValidity(fname: String, sname: String): Boolean {
        val nameRegex = Regex("^[A-Za-z]+$")

        return if (nameRegex.matches(fname) && nameRegex.matches(sname)) {
            true
        } else {
            Log.d("check name validity", "name is not completely of letters")
            false
        }
    }

    /*
    checks validity of the entries in day, month and year
    it's not valid, if:
        1. the entry is empty or not a number
        2. the year is higher than the current year
        3. the month is higher than 12
        4. the day and/or the month is less then 1
        5. the day is higher than the highest day in the respective month
        (e.g. in month 1(january) the day is higher than 31
     */
    private fun checkBirthdayValidity(day: String, month: String, year: String): Boolean {
        val dayInt = day.toIntOrNull()
        val monthInt = month.toIntOrNull()
        val yearInt = year.toIntOrNull()

        if (dayInt == null || monthInt == null || yearInt == null) {
            Log.d("check birthday validity", "entries in day, month and/or year are not  numbers")
            return false
        }

        if (yearInt > LocalDate.now().year) {
            Log.d("check birthday validity", "year is higher than current year")
            return false
        }

        if (dayInt < 1 || monthInt < 1 || monthInt > 12) {
            Log.d("check birthday validity", "day is lower than 1 or month is not between 1 or 12")
            return false
        }

        when (monthInt) {
            1 -> if (dayInt > 31) {
                return false
            }
            2 -> if ((yearInt % 4 == 0 && dayInt > 29) || (yearInt % 4 != 0 && dayInt > 28)) {
                return false
            }
            3 -> if (dayInt > 31) {
                return false
            }
            4 -> if (dayInt > 30) {
                return false
            }
            5 -> if (dayInt > 31) {
                return false
            }
            6 -> if (dayInt > 30) {
                return false
            }
            7 -> if (dayInt > 31) {
                return false
            }
            8 -> if (dayInt > 31) {
                return false
            }
            9 -> if (dayInt > 30) {
                return false
            }
            10 -> if (dayInt > 31) {
                return false
            }
            11 -> if (dayInt > 30) {
                return false
            }
            12 -> if (dayInt > 31) {
                return false
            }
        }

        return true
    }
}