package com.example.marcu.birthdays

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PersonAdapter(private var mctx: Context, private val personList: List<Person>): RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PersonViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(mctx)
        var view: View = inflater.inflate(R.layout.list_layout, null)
        return PersonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        var person = personList.get(position)
        holder.nameView.setText(person.firstName + " " + person.secondName)
        holder.ageView.setText(person.getAge())
        holder.birthdayView.setText(person.birthdayString)
    }

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameView = itemView.findViewById<TextView>(R.id.nameView)
        var ageView = itemView.findViewById<TextView>(R.id.ageView)
        var birthdayView = itemView.findViewById<TextView>(R.id.birthdayView)
    }
}