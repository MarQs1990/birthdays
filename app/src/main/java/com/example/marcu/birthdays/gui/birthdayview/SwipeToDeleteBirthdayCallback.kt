package com.example.marcu.birthdays.gui.birthdayview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.marcu.birthdays.R

class SwipeToDeleteBirthdayCallback(private val birthdayAdapter: BirthdayAdapter, private val context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val deleteIcon = ContextCompat.getDrawable(birthdayAdapter.getContext(), R.drawable.ic_delete)
    private val background = ColorDrawable(Color.RED)

    override fun onMove(
        p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(birthdayViewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = birthdayViewHolder.adapterPosition
        birthdayAdapter.removeBirthday(birthdayAdapter.getBirthday(position))
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        birthdayviewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, birthdayviewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = birthdayviewHolder.itemView
        val backgroundCornerOffset = 20

        when {
            dX > 0 -> {
                background.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
            }
            dX < 0 -> {
                background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
            }
            else -> {
                background.setBounds(0,0,0,0)
            }
        }

        background.draw(c)
    }
}