package com.example.marcu.birthdays.gui.birthdayview

import com.example.marcu.birthdays.birthdays.Birthday

interface OnBirthdayClickListener {
    fun onBirthdayClicked(birthday: Birthday)
}