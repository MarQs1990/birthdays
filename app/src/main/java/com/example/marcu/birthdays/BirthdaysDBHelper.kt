package com.example.marcu.birthdays

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BirthdaysDBHandler(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createPeopleTable = ("CREATE TABLE " + TABLE_PEOPLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRSTNAME + " TEXT,"
                + COLUMN_SECONDNAME + " TEXT,"
                + COLUMN_BIRTHDAY +  " TEXT,"
                + COLUMN_BIRTHDAYMONTH + " INTEGER" + ")")
        db.execSQL(createPeopleTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PEOPLE")
        onCreate(db)
    }

    companion object {

        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "birthdays.db"
        private const val TABLE_PEOPLE = "people"

        private const val COLUMN_ID = "_id"
        private const val COLUMN_FIRSTNAME = "firstname"
        private const val COLUMN_SECONDNAME = "secondname"
        private const val COLUMN_BIRTHDAY = "birthday"
        private const val COLUMN_BIRTHDAYMONTH = "birthdaymonth"
    }

    fun addPerson(person: Person, context: Context) {
        val values = ContentValues()
        values.put(COLUMN_FIRSTNAME, person.firstName)
        values.put(COLUMN_SECONDNAME, person.secondName)
        values.put(COLUMN_BIRTHDAY, person.birthdayString)
        values.put(COLUMN_BIRTHDAYMONTH, person.birthday.monthValue)

        val db = this.writableDatabase
        db.insert(TABLE_PEOPLE, null, values)
        db.close()

        Toast.makeText(context, "Eintrag hinzugefügt", Toast.LENGTH_LONG).show()
        println("${person.firstName} ${person.secondName} hinzugefügt")
    }

    fun editPerson(person: Person, context: Context){
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!
        val birthday = LocalDate.parse(person.birthdayString, formatter)

        val fname = person.firstName
        val sname = person.secondName
        val birthdayString = person.birthdayString

        val values = ContentValues()
        values.put(COLUMN_FIRSTNAME, fname)
        values.put(COLUMN_SECONDNAME, sname)
        values.put(COLUMN_BIRTHDAY, birthdayString)
        values.put(COLUMN_BIRTHDAYMONTH, birthday.monthValue)

        val query =
            "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_FIRSTNAME = \"$fname\" AND $COLUMN_SECONDNAME = \"$sname\" AND $COLUMN_BIRTHDAY = \"$birthdayString\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = cursor.getString(0)
            db.update(TABLE_PEOPLE, values, "_id = $id", null)
            cursor.close()
        }

        Toast.makeText(context, "Eintrag geändert", Toast.LENGTH_LONG).show()
    }

    fun deletePerson(fname: String, sname: String, birthdayString: String) {
        val query =
            "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_FIRSTNAME = \"$fname\" AND $COLUMN_SECONDNAME = \"$sname\" AND $COLUMN_BIRTHDAY = \"$birthdayString\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_PEOPLE, "$COLUMN_ID = ?", arrayOf(id.toString()))
            cursor.close()
        }
    }

    fun findAllPeople(month: Int): MutableList<Person>{

        val query = if (month == 13){
            "SELECT * FROM $TABLE_PEOPLE"
        }else {
            "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_BIRTHDAYMONTH = $month "
        }
        val people: MutableList<Person> = mutableListOf()

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()){
            people.add(Person(cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            while(cursor.moveToNext()){
                people.add(Person(cursor.getString(1), cursor.getString(2), cursor.getString(3)))
            }
        }
        cursor.close()
        db.close()

        return people
    }

    fun findPerson(fname: String, sname: String, birthdayString: String): Person? {

        val query =
            "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_FIRSTNAME = \"$fname\" AND $COLUMN_SECONDNAME = \"$sname\" AND $COLUMN_BIRTHDAY = \"$birthdayString\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var person: Person? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val firstName = cursor.getString(1)
            val secondName = cursor.getString(2)
            val birthday = cursor.getString(3)
            person = Person(firstName, secondName, birthday)
        }

        cursor.close()

        return person
    }

    fun findNextTenBirthdays(): MutableList<Person>{
        var allPeople = findAllPeople(13)
        val birthdaysBeforeToday: MutableList<Person> = mutableListOf()
        val birthdaysAfterToday: MutableList<Person> = mutableListOf()
        val today = LocalDate.now()

        val next10People: MutableList<Person> = mutableListOf()
        allPeople = bubbleSortMonth(allPeople)
        allPeople = bubbleSortDay(allPeople)

        for (person in allPeople){
            if (person.birthday.monthValue < today.monthValue){
                birthdaysBeforeToday.add(person)
            } else if (person.birthday.monthValue == today.monthValue &&
                    person.birthday.dayOfMonth < today.dayOfMonth){
                birthdaysBeforeToday.add(person)
            } else {
                birthdaysAfterToday.add(person)
            }
        }

        if(birthdaysAfterToday.size <= 10){
            for (i in 0 until birthdaysAfterToday.size){
                next10People.add(birthdaysAfterToday[i])
            }
            if (birthdaysBeforeToday.size <= 10 - birthdaysAfterToday.size){
                for (i in 0 until birthdaysBeforeToday.size){
                    next10People.add(birthdaysBeforeToday[i])
                }
            } else {
                for (i in 0 until 10 - birthdaysAfterToday.size){
                    next10People.add(birthdaysBeforeToday[i])
                }
            }

        } else {
            for (i in 0..9){
                next10People.add(birthdaysAfterToday[i])
            }
        }

        return next10People
    }

    private fun bubbleSortMonth(allPeople: MutableList<Person>): MutableList<Person>{
        for (pass in 0 until(allPeople.size - 1)){
            for (currentPosition in 0 until (allPeople.size - pass - 1)){
                if (allPeople[currentPosition].birthday.monthValue > allPeople[currentPosition + 1].birthday.monthValue){
                    val tmp = allPeople[currentPosition]
                    allPeople[currentPosition] = allPeople[currentPosition + 1]
                    allPeople[currentPosition + 1] = tmp
                }
            }
        }
        return allPeople
    }

    private fun bubbleSortDay(allPeople: MutableList<Person>): MutableList<Person>{
        for (pass in 0 until(allPeople.size - 1)){
            for (currentPosition in 0 until (allPeople.size - pass - 1)){
                if (allPeople[currentPosition].birthday.monthValue == allPeople[currentPosition + 1].birthday.monthValue &&
                    allPeople[currentPosition].birthday.dayOfMonth > allPeople[currentPosition + 1].birthday.dayOfMonth){
                    val tmp = allPeople[currentPosition]
                    allPeople[currentPosition] = allPeople[currentPosition + 1]
                    allPeople[currentPosition + 1] = tmp
                }
            }
        }
        return allPeople
    }
}
