package com.example.marcu.birthdays.gui.birthdayview

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.marcu.birthdays.birthdays.Birthday
import com.example.marcu.birthdays.R
import com.example.marcu.birthdays.birthdays.BirthdaysDBHandler
import kotlinx.android.synthetic.main.activity_new_person.view.*
import java.time.LocalDate

class BirthdayAdapter(
    private val context: Context,
    private val birthdayList: MutableList<Birthday>,
    private val birthdayClickListener: OnBirthdayClickListener
) : RecyclerView.Adapter<BirthdayViewHolder>() {
    
    private val dbHandler = BirthdaysDBHandler(context)

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BirthdayViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_layout, null)
        return BirthdayViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return birthdayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BirthdayViewHolder, position: Int) {
        val birthday = birthdayList[position]
        holder.bind(birthday, birthdayClickListener)
    }

    fun getBirthday(position: Int): Birthday {
        return birthdayList[position]
    }

    fun getContext(): Context{
        return context
    }

    fun addBirthday(birthday: Birthday){
        birthdayList.add(birthday)
        notifyItemInserted(birthdayList.indexOf(birthday))
        dbHandler.addBirthday(birthday)
    }

    fun removeBirthday(birthday: Birthday){
        val birthdayIndex = birthdayList.indexOf(birthday)
        birthdayList.remove(birthday)
        notifyItemRemoved(birthdayIndex)
        notifyItemRangeChanged(birthdayIndex, itemCount)
        dbHandler.deleteBirthday(birthday)
    }

    fun editBirthday(oldBirthday: Birthday, newBirthday: Birthday){
        val birthdayIndex = birthdayList.indexOf(oldBirthday)
        birthdayList[birthdayIndex] = newBirthday
        dbHandler.editBirthday(oldBirthday, newBirthday)
        notifyItemChanged(birthdayIndex)
    }
}
