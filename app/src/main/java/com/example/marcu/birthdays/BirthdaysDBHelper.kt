package com.example.marcu.birthdays

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BirthdaysDBHandler(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PEOPLE_TABLE = ("CREATE TABLE " + TABLE_PEOPLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRSTNAME + " TEXT,"
                + COLUMN_SECONDNAME + " TEXT,"
                + COLUMN_BIRTHDAY +  " TEXT,"
                + COLUMN_BIRTHDAYMONTH + " INTEGER" + ")")
        db.execSQL(CREATE_PEOPLE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE)
        onCreate(db)
    }

    companion object {

        private val DATABASE_VERSION = 4
        private val DATABASE_NAME = "birthdays.db"
        val TABLE_PEOPLE = "people"

        val COLUMN_ID = "_id"
        val COLUMN_FIRSTNAME = "firstname"
        val COLUMN_SECONDNAME = "secondname"
        val COLUMN_BIRTHDAY = "birthday"
        val COLUMN_BIRTHDAYMONTH = "birthdaymonth"
    }

    fun addPerson(person: Person) {
        val values = ContentValues()
        values.put(COLUMN_FIRSTNAME, person.firstName)
        values.put(COLUMN_SECONDNAME, person.secondName)
        values.put(COLUMN_BIRTHDAY, person.birthdayString)
        values.put(COLUMN_BIRTHDAYMONTH, person.birthday.monthValue)

        println(person.firstName)
        println(person.secondName)
        println(person.birthdayString)
        println(person.birthday.monthValue)

        val db = this.writableDatabase
        db.insert(TABLE_PEOPLE, null, values)
        db.close()
    }

    fun findallPeople(month: Int): MutableList<Person>{
        val query: String
        if (month == 13){
            query = "SELECT * FROM $TABLE_PEOPLE"
        }else {
            query = "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_BIRTHDAYMONTH = $month "
        }
        var people: MutableList<Person> = mutableListOf()

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

    fun findPerson(fname: String, sname: String): Person? {
        //TODO Birthday in Query einbauen, damit die Suche eindeutiger ist (gleicher Name mehrerer Personen)

        val query =
            "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_FIRSTNAME = \"$fname\" AND $COLUMN_SECONDNAME = \"$sname\""

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

    fun deletePerson(fname: String, sname: String): Boolean {
        //TODO Birthday in Query einbauen, damit die Suche eindeutiger ist (gleicher Name mehrerer Personen)

        var result = false

        val query =
            "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_FIRSTNAME = \"$fname\" AND $COLUMN_SECONDNAME = \"$sname\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_PEOPLE, COLUMN_ID + " = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }

        return result
    }

    fun editPerson(person: Person, fname:String, sname: String, birthday: String): Boolean{
        //TODO Implementierung der Logik für bearbeiten einer Person (entweder Eintrag bearbeiten oder löschen + hinzufügen)
        return true
    }
}