package com.gmail.echomskfan.persons.ui.casts

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.CastVM

class CastsListAdapter(private val context: Context, private val presenter: CastsPresenter) : RecyclerView.Adapter<CastsListAdapter.Holder>() {

    private val items = mutableListOf<CastVM>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater
                .from(parent.context).inflate(R.layout.item_cast, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setItem(items[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setItem(item: CastVM) {
        }
    }
}