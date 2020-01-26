package com.example.marcu.birthdays.gui.activities

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.birthdays.Birthday
import com.example.marcu.birthdays.exceptions.IllegalDateException
import com.example.marcu.birthdays.exceptions.IllegalNameException
import com.example.marcu.birthdays.gui.birthdayview.BirthdayAdapter
import kotlinx.android.synthetic.main.activity_new_person.view.*

class SaveEditBirthdayDialog {

    private val context: Context
    private val birthdayAdapter: BirthdayAdapter
    private var fname = ""
    private var sname = ""
    private var birthdayDay = ""
    private var birthdayMonth = ""
    private var birthdayYear = ""
    private var isNewBirthday = true
    private lateinit var oldBirthday: Birthday

    //Constructor for a new birthday
    constructor(_context: Context, _birthdayAdapter: BirthdayAdapter){
        context = _context
        birthdayAdapter = _birthdayAdapter
    }

    //Constructor for editing a birthday
    constructor(_context: Context, _birthdayAdapter: BirthdayAdapter, _birthday: Birthday){
        isNewBirthday = false
        context = _context
        birthdayAdapter = _birthdayAdapter
        oldBirthday = _birthday
        fname = _birthday.firstName
        sname = _birthday.secondName
        birthdayDay = _birthday.birthday.dayOfMonth.toString()
        birthdayMonth = _birthday.birthday.monthValue.toString()
        birthdayYear = _birthday.birthday.year.toString()

    }

    fun setupDialog(){
        val saveBirthdayView = LayoutInflater.from(context).inflate(R.layout.activity_new_person, null)
        saveBirthdayView.fnameText.text = Editable.Factory.getInstance().newEditable(fname)
        saveBirthdayView.snameText.text = Editable.Factory.getInstance().newEditable(sname)
        saveBirthdayView.dayText.text = Editable.Factory.getInstance().newEditable(birthdayDay)
        saveBirthdayView.monthText.text = Editable.Factory.getInstance().newEditable(birthdayMonth)
        saveBirthdayView.yearText.text = Editable.Factory.getInstance().newEditable(birthdayYear)

        val saveBirthdayViewBuilder = AlertDialog.Builder(context).setView(saveBirthdayView)
            .setTitle("Füge einen Geburtstag hinzu")

        val alertDialog = saveBirthdayViewBuilder.show()

        setOnClickListener(saveBirthdayView, alertDialog)
    }

    private fun setOnClickListener(saveBirthdayView: View, alertDialog: AlertDialog){
        saveBirthdayView.savePersonButton.setOnClickListener {
            try {
                val birthday = Birthday.generateBirthdayFromView(saveBirthdayView)
                if (isNewBirthday) birthdayAdapter.addBirthday(birthday) else birthdayAdapter.editBirthday(oldBirthday, birthday)


                alertDialog.dismiss()
            } catch (e: IllegalNameException) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            } catch (e: IllegalDateException) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        saveBirthdayView.cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}