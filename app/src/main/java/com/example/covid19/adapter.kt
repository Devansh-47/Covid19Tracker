package com.example.covid19

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.layout_simple.view.*

class adapter(var context:Context,var list:ArrayList<responseclass>):BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }


    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view=LayoutInflater.from(context).inflate(R.layout.layout_simple,parent,false)
        view.tc_simple.text="${list[position].confirmed}"
        view.a_simple.text="${list[position].active}"
        view.d_simple.text="${list[position].deaths}"
        view.state_simple.text="${list[position].province}"

        return view
    }

}