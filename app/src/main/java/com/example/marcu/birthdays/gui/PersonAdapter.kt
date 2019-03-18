package com.example.marcu.birthdays.gui

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.view.ContextMenu
import android.view.View.OnCreateContextMenuListener
import com.example.marcu.birthdays.core.Person
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.core.MENU_EDIT
import com.example.marcu.birthdays.core.MENU_REMOVE

class PersonAdapter(private var context: Context, private val personList: List<Person>): RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(){

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PersonViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_layout, null)
        return PersonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.nameView.text = person.firstName + " " + person.secondName
        holder.ageView.text = person.getAge()
        holder.birthdayView.text = person.birthdayString
    }

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnCreateContextMenuListener {

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu!!.add(this.adapterPosition, MENU_EDIT, 0, "bearbeiten")
            menu.add(this.adapterPosition, MENU_REMOVE, 0, "löschen")
        }

        var nameView = itemView.findViewById<TextView>(R.id.nameView)!!
        var ageView = itemView.findViewById<TextView>(R.id.ageView)!!
        var birthdayView = itemView.findViewById<TextView>(R.id.birthdayView)!!

        init {
            itemView.setOnCreateContextMenuListener(this)
        }
    }
}