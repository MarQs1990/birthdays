package com.example.marcu.birthdays

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ExpandableListAdapter(context: Context, listHeaderData: MutableList<String>, listChildData: MutableMap<String, MutableList<String>>): BaseExpandableListAdapter() {

    private lateinit var _context: Context
    private var header: List<String>
    private var child: MutableMap<String, MutableList<String>>

    init {
        _context = context
        header = listHeaderData
        child = listChildData
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val headerTitle = getGroup(groupPosition).toString()
        val retView: View

        retView = if (convertView == null){
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.header, parent, false)
        } else {
            convertView
        }

        val headerTextView = retView.findViewById<TextView>(R.id.header)
        headerTextView.text = headerTitle

        if(getChildrenCount(groupPosition) > 0){
            if(isExpanded){
                headerTextView.setTypeface(null, Typeface.BOLD)
                headerTextView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.arrow_up, 0)
            } else {
                headerTextView.setTypeface(null, Typeface.NORMAL)
                headerTextView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.arrow_down, 0)
            }
        }


        return retView
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        val childText = getChild(groupPosition, childPosition).toString()
        val retView: View?

        if (convertView == null){
            val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            retView = inflater.inflate(R.layout.child, parent, false)
        } else {
            retView = convertView
        }

        val childTextView = retView!!.findViewById<TextView>(R.id.child)
        childTextView.text = childText

        return retView
    }

    override fun getGroup(groupPosition: Int): Any {
        return header[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return child[header[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return Integer.toUnsignedLong(groupPosition)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return Integer.toUnsignedLong(childPosition)
    }

    override fun getGroupCount(): Int {
        return header.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        var childCount = 0
        if(groupPosition == 1){
            childCount = child[header[groupPosition]]!!.size
        }
        return childCount
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

}