package com.example.marcu.birthdays.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.marcu.birthdays.core.BirthdaysDBHandler
import com.example.marcu.birthdays.Person
import com.example.marcu.birthdays.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SavePersonActivity : AppCompatActivity() {

    private lateinit var fnameText: EditText
    private lateinit var snameText: EditText
    private lateinit var dayText: EditText
    private lateinit var monthText: EditText
    private lateinit var yearText: EditText
    private lateinit var savePersonButton: Button
    private lateinit var cancelButton: Button

    private lateinit var dbHandler: BirthdaysDBHandler

    private var isIntent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_person)

        val intent = intent
        dbHandler = BirthdaysDBHandler(this)

        fnameText = findViewById(R.id.fnameText)
        snameText = findViewById(R.id.snameText)
        dayText = findViewById(R.id.dayText)
        monthText = findViewById(R.id.monthText)
        yearText = findViewById(R.id.yearText)
        savePersonButton = findViewById(R.id.savePersonButton)
        cancelButton= findViewById(R.id.cancelButton)

        //Checks whether this activity is called for saving a new person oder editing an existing person
        if (intent.extras != null){
            setTexts(intent)
            isIntent = true
        }

        initiateToolbar()
    }

    @SuppressLint("RestrictedApi")
    private fun initiateToolbar(){
        val actionBar = supportActionBar
        actionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        if (isIntent){
            actionBar.title = getString(R.string.headline_edit_person)
        } else {
            actionBar.title = getString(R.string.headline_new_person)
        }
    }

    fun onClickButton(view: View){
        if (isIntent){
            saveEditPerson()
        } else {
            saveNewPerson()
        }
    }

    private fun saveNewPerson() {

        val person = getPerson()

        if (person != null) {
            dbHandler.addPerson(person, applicationContext)

            Toast.makeText(this, resources.getString(R.string.saved_new_birthday), Toast.LENGTH_LONG).show()

            goToBirthdays()
        } else {
            Toast.makeText(this, getString(R.string.save_person_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun saveEditPerson(){

        val person = getPerson()

        if (person != null) {
            dbHandler.editPerson(person, applicationContext)

            Toast.makeText(this, getString(R.string.edited_birthday), Toast.LENGTH_LONG).show()

            goToBirthdays()
        } else {
            Toast.makeText(this, getString(R.string.save_person_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun getPerson(): Person? {
        val fname = fnameText.text.toString()
        val sname = snameText.text.toString()
        var day = dayText.text.toString()
        var month = monthText.text.toString()
        val year = yearText.text.toString()

        if (!checkNameValidity(fname, sname)){ return null }

        if (!checkBirthdayValidity(day, month, year)) { return null }

        if (day.length == 1){
            day = "0$day"
        }

        if (month.length == 1){
            month = "0$month"
        }

        val birthday = "$day.$month.$year"

        return Person(fname, sname, birthday)
    }

    //sets the texts in the text fields, if editing an entry
    private fun setTexts(intent: Intent){
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!
        val birthday = LocalDate.parse(intent.getStringExtra("birthday"), formatter)

        fnameText.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("first name"))
        snameText.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("second name"))
        if (birthday.dayOfMonth.toString().length == 1){
            dayText.text = Editable.Factory.getInstance().newEditable("0" + birthday.dayOfMonth.toString())
        } else {
            dayText.text = Editable.Factory.getInstance().newEditable(birthday.dayOfMonth.toString())
        }
        if (birthday.monthValue.toString().length == 1){
            monthText.text = Editable.Factory.getInstance().newEditable("0" + birthday.monthValue.toString())
        } else {
            monthText.text = Editable.Factory.getInstance().newEditable(birthday.monthValue.toString())
        }
        yearText.text = Editable.Factory.getInstance().newEditable(birthday.year.toString())
    }

    fun cancel(view:View) {
        Toast.makeText(view.context, "Vorgang abgebrochen", Toast.LENGTH_LONG).show()
        goToBirthdays()
    }

    private fun goToBirthdays(){
        val intent = Intent(this, BirthdaysActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId){
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)
    }

    /*
    checks the validity of the entries in first name and second name
    it's not valid, if the entry is empty or not just letters
     */
    private fun checkNameValidity(fname: String, sname:String): Boolean{
        val nameRegex = Regex("^[A-Za-z]+$")

        return if (nameRegex.matches(fname) && nameRegex.matches(sname)){
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
    private fun checkBirthdayValidity(day: String, month: String, year: String): Boolean{
        val dayInt = day.toIntOrNull()
        val monthInt = month.toIntOrNull()
        val yearInt = year.toIntOrNull()

        if (dayInt == null || monthInt == null || yearInt == null){
            Log.d("check birthday validity", "entries in day, month and/or year are not  numbers")
            return false
        }

        if (yearInt > LocalDate.now().year){
            Log.d("check birthday validity", "year is higher than current year")
            return false
        }

        if (dayInt < 1 || monthInt < 1 || monthInt > 12){
            Log.d("check birthday validity", "day is lower than 1 or month is not between 1 or 12")
            return false
        }

        when (monthInt){
            1 -> if (dayInt > 31){ return false }
            2 -> if ((yearInt % 4 == 0 && dayInt > 29) || (yearInt % 4 != 0 && dayInt > 28)) { return false }
            3 -> if (dayInt > 31){ return false }
            4 -> if (dayInt > 30){ return false }
            5 -> if (dayInt > 31){ return false }
            6 -> if (dayInt > 30){ return false }
            7 -> if (dayInt > 31){ return false }
            8 -> if (dayInt > 31){ return false }
            9 -> if (dayInt > 30){ return false }
            10 -> if (dayInt > 31){ return false }
            11 -> if (dayInt > 30){ return false }
            12 -> if (dayInt > 31){ return false }
        }

        return true
    }
}
