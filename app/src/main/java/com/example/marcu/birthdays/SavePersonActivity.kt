package com.example.marcu.birthdays

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        dbHandler.addPerson(person, applicationContext)

        goToBirthdays()
    }

    private fun saveEditPerson(){

        val person = getPerson()

        println("${person.firstName} ${person.secondName} ${person.birthdayString}")

        dbHandler.editPerson(person, applicationContext)

        goToBirthdays()
    }

    private fun getPerson(): Person {
        val fname = fnameText.text.toString()
        val sname = snameText.text.toString()
        var day = dayText.text.toString()
        var month = monthText.text.toString()
        val year = yearText.text.toString()

        if (day.length == 1){
            day = "0$day"
        }

        if (month.length == 1){
            month = "0$month"
        }

        val birthday = "$day.$month.$year"

        return Person(fname, sname, birthday)
    }

    private fun setTexts(intent: Intent){
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!
        val birthday = LocalDate.parse(intent.getStringExtra("birthday"), formatter)

        fnameText.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("first name"))
        snameText.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("second name"))
        dayText.text = Editable.Factory.getInstance().newEditable(birthday.dayOfMonth.toString())
        monthText.text = Editable.Factory.getInstance().newEditable(birthday.monthValue.toString())
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
}
