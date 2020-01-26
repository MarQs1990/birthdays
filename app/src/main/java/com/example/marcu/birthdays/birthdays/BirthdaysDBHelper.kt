package com.example.marcu.birthdays.birthdays

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BirthdaysDBHandler(private var context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        val createPeopleTable =
            ("CREATE TABLE " + TABLE_PEOPLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_FIRSTNAME + " TEXT," + COLUMN_SECONDNAME + " TEXT," + COLUMN_BIRTHDAY + " TEXT," + COLUMN_BIRTHDAYMONTH + " INTEGER" + ")")
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

    fun addBirthday(birthday: Birthday) {
        val values = ContentValues()
        values.put(COLUMN_FIRSTNAME, birthday.firstName)
        values.put(COLUMN_SECONDNAME, birthday.secondName)
        values.put(COLUMN_BIRTHDAY, birthday.birthdayString)
        values.put(COLUMN_BIRTHDAYMONTH, birthday.birthday.monthValue)

        val db = this.writableDatabase
        db.insert(TABLE_PEOPLE, null, values)
        db.close()

        Toast.makeText(context, "Eintrag hinzugefügt", Toast.LENGTH_LONG).show()
        println("${birthday.firstName} ${birthday.secondName} hinzugefügt")
    }

    fun editBirthday(birthday: Birthday) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")!!
        val birthdayDate = LocalDate.parse(birthday.birthdayString, formatter)

        val fname = birthday.firstName
        val sname = birthday.secondName
        val birthdayString = birthday.birthdayString

        val values = ContentValues()
        values.put(COLUMN_FIRSTNAME, fname)
        values.put(COLUMN_SECONDNAME, sname)
        values.put(COLUMN_BIRTHDAY, birthdayString)
        values.put(COLUMN_BIRTHDAYMONTH, birthdayDate.monthValue)

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

    fun deleteBirthday(fname: String, sname: String, birthdayString: String) {
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

    fun deleteBirthday(birthday: Birthday) {
        val fname = birthday.firstName
        val sname = birthday.secondName
        val birthdayString = birthday.birthdayString
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

    fun getAllBirthdays(): MutableList<Birthday> {
        val query = "SELECT * FROM $TABLE_PEOPLE"

        val birthdays: MutableList<Birthday> = mutableListOf()

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            birthdays.add(
                Birthday(
                    cursor.getString(1), cursor.getString(2), cursor.getString(3)
                )
            )
            while (cursor.moveToNext()) {
                birthdays.add(
                    Birthday(
                        cursor.getString(1), cursor.getString(2), cursor.getString(3)
                    )
                )
            }
        }
        cursor.close()
        db.close()

        return birthdays
    }

    fun getAllBirthdaysByMonth(month: Int): MutableList<Birthday> {
        val query = "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_BIRTHDAYMONTH = $month "
        val birthdays: MutableList<Birthday> = mutableListOf()

        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            birthdays.add(
                Birthday(
                    cursor.getString(1), cursor.getString(2), cursor.getString(3)
                )
            )
            while (cursor.moveToNext()) {
                birthdays.add(
                    Birthday(
                        cursor.getString(1), cursor.getString(2), cursor.getString(3)
                    )
                )
            }
        }
        cursor.close()
        db.close()

        return birthdays
    }

    fun findBirthday(fname: String, sname: String, birthdayString: String): Birthday? {

        val query =
            "SELECT * FROM $TABLE_PEOPLE WHERE $COLUMN_FIRSTNAME = \"$fname\" AND $COLUMN_SECONDNAME = \"$sname\" AND $COLUMN_BIRTHDAY = \"$birthdayString\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var birthday: Birthday? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val firstName = cursor.getString(1)
            val secondName = cursor.getString(2)
            val birthdayDate = cursor.getString(3)
            birthday = Birthday(
                firstName,
                secondName,
                birthdayDate
            )
        }

        cursor.close()

        return birthday
    }

    fun findNextTenBirthdays(): MutableList<Birthday> {
        var allBirthdays = getAllBirthdays()
        val birthdaysBeforeToday: MutableList<Birthday> = mutableListOf()
        val birthdaysAfterToday: MutableList<Birthday> = mutableListOf()
        val today = LocalDate.now()

        val next10Birthdays: MutableList<Birthday> = mutableListOf()
        allBirthdays = bubbleSortMonth(allBirthdays)
        allBirthdays = bubbleSortDay(allBirthdays)

        for (birthday in allBirthdays) {
            if (birthday.birthday.monthValue < today.monthValue) {
                birthdaysBeforeToday.add(birthday)
            } else if (birthday.birthday.monthValue == today.monthValue && birthday.birthday.dayOfMonth < today.dayOfMonth) {
                birthdaysBeforeToday.add(birthday)
            } else {
                birthdaysAfterToday.add(birthday)
            }
        }

        if (birthdaysAfterToday.size <= 10) {
            for (i in 0 until birthdaysAfterToday.size) {
                next10Birthdays.add(birthdaysAfterToday[i])
            }
            if (birthdaysBeforeToday.size <= 10 - birthdaysAfterToday.size) {
                for (i in 0 until birthdaysBeforeToday.size) {
                    next10Birthdays.add(birthdaysBeforeToday[i])
                }
            } else {
                for (i in 0 until 10 - birthdaysAfterToday.size) {
                    next10Birthdays.add(birthdaysBeforeToday[i])
                }
            }

        } else {
            for (i in 0..9) {
                next10Birthdays.add(birthdaysAfterToday[i])
            }
        }

        return next10Birthdays
    }

    //Bubble Sort Algorithm to sort the birthdays by month and day
    private fun bubbleSortMonth(allBirthdays: MutableList<Birthday>): MutableList<Birthday> {
        for (pass in 0 until (allBirthdays.size - 1)) {
            for (currentPosition in 0 until (allBirthdays.size - pass - 1)) {
                if (allBirthdays[currentPosition].birthday.monthValue > allBirthdays[currentPosition + 1].birthday.monthValue) {
                    val tmp = allBirthdays[currentPosition]
                    allBirthdays[currentPosition] = allBirthdays[currentPosition + 1]
                    allBirthdays[currentPosition + 1] = tmp
                }
            }
        }
        return allBirthdays
    }

    private fun bubbleSortDay(allBirthdays: MutableList<Birthday>): MutableList<Birthday> {
        for (pass in 0 until (allBirthdays.size - 1)) {
            for (currentPosition in 0 until (allBirthdays.size - pass - 1)) {
                if (allBirthdays[currentPosition].birthday.monthValue == allBirthdays[currentPosition + 1].birthday.monthValue && allBirthdays[currentPosition].birthday.dayOfMonth > allBirthdays[currentPosition + 1].birthday.dayOfMonth) {
                    val tmp = allBirthdays[currentPosition]
                    allBirthdays[currentPosition] = allBirthdays[currentPosition + 1]
                    allBirthdays[currentPosition + 1] = tmp
                }
            }
        }
        return allBirthdays
    }
}
