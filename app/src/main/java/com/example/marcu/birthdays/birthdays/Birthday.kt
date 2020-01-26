package com.example.marcu.birthdays.birthdays

import android.view.View
import kotlinx.android.synthetic.main.activity_new_person.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Birthday(){

    lateinit var firstName: String
    lateinit var secondName: String
    lateinit var birthdayString: String
    lateinit var birthday: LocalDate

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!

    constructor(_firstName: String, _secondName: String, _birthdayString: String): this(){
        firstName = _firstName.capitalize()
        secondName = _secondName.capitalize()
        this.birthdayString = _birthdayString
        birthday = LocalDate.parse(birthdayString, formatter)
    }

    constructor(_firstName: String, _secondName: String, birthdayDay:String, birthdayMonth:String, birthdayYear: String): this(){
        firstName = _firstName.capitalize()
        secondName = _secondName.capitalize()
        this.birthdayString = generateBirthdayString(birthdayDay, birthdayMonth, birthdayYear)
        birthday = LocalDate.parse(birthdayString, formatter)
    }

    fun getAge(): Int{

        val currentDate = LocalDate.now()

        val birthdayYear = birthday.year
        val birthdayMonth = birthday.month
        val birthdayDay = birthday.dayOfMonth

        val currentYear = currentDate.year
        val currentMonth = currentDate.month
        val currentDay = currentDate.dayOfMonth

        return if (birthdayMonth == currentMonth){
            if (birthdayDay < currentDay){
                currentYear - birthdayYear - 1
            } else{
                currentYear - birthdayYear
            }
        } else if (birthdayMonth > currentMonth){
            currentYear - birthdayYear - 1
        } else{
            currentYear - birthdayYear
        }
    }

    private fun generateBirthdayString(_birthdayDay: String, _birthdayMonth: String, _birthdayYear: String): String{
        val birthdayDay: String = if (_birthdayDay.length == 1) {
            "0$_birthdayDay"
        } else {
            _birthdayDay
        }
        val birthdayMonth: String = if (_birthdayMonth.length == 1) {
            "0$_birthdayMonth"
        } else {
            _birthdayMonth
        }

        return "$birthdayDay.$birthdayMonth.$_birthdayYear"
    }


    companion object {
        fun generateBirthdayFromView(view: View): Birthday {
            val fname = view.fnameText.text.toString()
            val sname = view.snameText.text.toString()
            val birthdayDay = view.dayText.text.toString()
            val birthdayMonth = view.monthText.text.toString()
            val birthdayYear = view.yearText.text.toString()

            checkNameValidity(fname, sname)
            checkBirthdayValidity(birthdayDay, birthdayMonth, birthdayYear)

            return Birthday(
                fname,
                sname,
                birthdayDay,
                birthdayMonth,
                birthdayYear
            )
        }

        private fun checkNameValidity(fname: String, sname: String) {
            val nameRegex = Regex("^[A-Za-z]+$")

            require(!(!nameRegex.matches(fname) && !nameRegex.matches(sname))) { "Name is not a valid name" }
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
        private fun checkBirthdayValidity(day: String, month: String, year: String) {
            val dayInt = day.toIntOrNull()
            val monthInt = month.toIntOrNull()
            val yearInt = year.toIntOrNull()

            require(!(dayInt == null || monthInt == null || yearInt == null)) { "Entries in day, month and/or year are not  numbers" }

            require(yearInt <= LocalDate.now().year) { "Year is higher than current year" }

            require(!(dayInt < 1 || monthInt < 1 || monthInt > 12)) { "Day is lower than 1 or month is not between 1 or 12" }

            when (monthInt) {
                1, 3, 5, 7, 8, 10, 12 -> require(dayInt <= 31) { "In month $monthInt day of birthday can't be $dayInt" }
                2 -> require(!((yearInt % 4 == 0 && dayInt > 29) || (yearInt % 4 != 0 && dayInt > 28))) { "In month $monthInt day of birthday can't be $dayInt" }
                4, 6, 9, 11 -> require(dayInt <= 30) { "In month $monthInt day of birthday can't be $dayInt" }
            }
        }
    }
}