package com.wujia.jetpack.paging3.sample.ui.separator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wujia.jetpack.paging3.sample.R

class SeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val description: TextView = view.findViewById(R.id.separator_description)

    fun bind(separatorText: String) {
        description.text = separatorText
    }

    companion object {
        fun create(parent: ViewGroup): SeparatorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.separator_view, parent, false)
            return SeparatorViewHolder(view)
        }
    }

}