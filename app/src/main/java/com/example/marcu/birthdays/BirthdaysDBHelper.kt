package com.example.marcu.birthdays

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
        SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PEOPLE_TABLE = ("CREATE TABLE " + TABLE_PEOPLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRSTNAME + " TEXT," + COLUMN_SECONDNAME + " TEXT," + COLUMN_BIRTHDAY + " TEXT" + ")")
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
    }

    fun addPERSON(person: Person) {
        val values = ContentValues()
        values.put(COLUMN_FIRSTNAME, person.firstName)
        values.put(COLUMN_SECONDNAME, person.secondName)
        values.put(COLUMN_BIRTHDAY, person.birthdayString)

        val db = this.writableDatabase
        db.insert(TABLE_PEOPLE, null, values)
        db.close()
    }

    fun findPerson(fname: String, sname: String): Person? {

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
}