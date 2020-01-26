package com.example.marcu.birthdays.gui.birthdayview

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.View
import android.widget.TextView
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.core.MENU_EDIT
import com.example.marcu.birthdays.core.MENU_REMOVE
import com.example.marcu.birthdays.birthdays.Birthday

class BirthdayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnCreateContextMenuListener {

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu!!.add(this.adapterPosition, MENU_EDIT, 0, "bearbeiten")
        menu.add(this.adapterPosition, MENU_REMOVE, 0, "löschen")
    }

    var nameView = itemView.findViewById<TextView>(R.id.nameView)!!
    var ageView = itemView.findViewById<TextView>(R.id.ageView)!!
    var birthdayView = itemView.findViewById<TextView>(R.id.birthdayView)!!

    @SuppressLint("SetTextI18n")
    fun bind(birthday: Birthday, clickListener: OnBirthdayClickListener) {
        nameView.text = birthday.firstName + " " + birthday.secondName
        ageView.text = birthday.getAge().toString()
        birthdayView.text = birthday.birthdayString

        itemView.setOnClickListener {
            clickListener.onBirthdayClicked(birthday)
        }
        itemView.setOnCreateContextMenuListener(this)
    }
}