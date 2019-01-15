package com.example.marcu.birthdays

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.view.ContextMenu
import android.view.View.OnCreateContextMenuListener

class PersonAdapter(private var mctx: Context, private val personList: List<Person>): RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(){

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PersonViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(mctx)
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
        private val MENU_REMOVE = 1
        private val MENU_EDIT = 2

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu!!.setHeaderTitle("Wähle eine Option")
            menu.add(this.adapterPosition, MENU_REMOVE, 0, "löschen")
            menu.add(this.adapterPosition, MENU_EDIT, 0, "bearbeiten")
        }

        var nameView = itemView.findViewById<TextView>(R.id.nameView)!!
        var ageView = itemView.findViewById<TextView>(R.id.ageView)!!
        var birthdayView = itemView.findViewById<TextView>(R.id.birthdayView)!!

        init {
            itemView.setOnCreateContextMenuListener(this)
        }


    }
}