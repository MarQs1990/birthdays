package com.example.marcu.birthdays

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_new_person.*

class NewPersonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_person)
    }

    fun saveNewPerson(view: View) {
        val dbHandler = BirthdaysDBHandler(this)

        val fname = fnameText.text.toString()
        val sname = snameText.text.toString()
        val day = dayText.text.toString()
        val month = monthText.text.toString()
        val year = yearText.text.toString()

        val birthday = "$day.$month.$year"

        val person = Person(fname, sname, birthday)

        dbHandler.addPerson(person, this)

        goToBirthdays()
    }

    fun cancel(view:View) {
        goToBirthdays()
    }

    private fun goToBirthdays(){
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }


}
