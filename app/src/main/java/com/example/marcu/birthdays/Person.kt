package com.example.marcu.birthdays

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
class Person(fName: String, sName: String, birthdayStr:String){

    val firstName: String
    val secondName: String
    val birthdayString = birthdayStr
    val birthday: LocalDate

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    init{
        firstName = fName.capitalize()
        secondName = sName.capitalize()
        birthday = LocalDate.parse(birthdayString, formatter)
    }

    fun getAge(): String{
        var age:String

        val currentDate = LocalDate.now()

        val birthdayYear = birthday.year
        val birthdayMonth = birthday.month
        val birthdayDay = birthday.dayOfMonth

        val currentYear = currentDate.year
        val currentMonth = currentDate.month
        val currentDay = currentDate.dayOfMonth

        if (birthdayMonth == currentMonth){
            if (birthdayDay < currentDay){
                age = Integer.toString(currentYear - birthdayYear - 1)
            } else{
                age = Integer.toString(currentYear - birthdayYear)
            }
        } else if (birthdayMonth > currentMonth){
            age =  Integer.toString(currentYear - birthdayYear - 1)
        } else{
            age = Integer.toString(currentYear - birthdayYear)
        }

        return age
    }
}