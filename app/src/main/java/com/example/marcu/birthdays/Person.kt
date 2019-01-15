package com.example.marcu.birthdays

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Person(fName: String, sName: String, birthdayStr:String){

    val firstName: String = fName.capitalize()
    val secondName: String = sName.capitalize()
    val birthdayString = birthdayStr
    val birthday: LocalDate

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!

    init{
        birthday = LocalDate.parse(birthdayString, formatter)
    }

    fun getAge(): String{
        val age:String

        val currentDate = LocalDate.now()

        val birthdayYear = birthday.year
        val birthdayMonth = birthday.month
        val birthdayDay = birthday.dayOfMonth

        val currentYear = currentDate.year
        val currentMonth = currentDate.month
        val currentDay = currentDate.dayOfMonth

        age = if (birthdayMonth == currentMonth){
            if (birthdayDay < currentDay){
                Integer.toString(currentYear - birthdayYear - 1)
            } else{
                Integer.toString(currentYear - birthdayYear)
            }
        } else if (birthdayMonth > currentMonth){
            Integer.toString(currentYear - birthdayYear - 1)
        } else{
            Integer.toString(currentYear - birthdayYear)
        }

        return age
    }
}