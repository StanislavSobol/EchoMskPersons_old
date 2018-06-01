package com.gmail.echomskfan.persons.ui.vips

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.VipVM
import kotlinx.android.synthetic.main.vip_item.view.*
import java.util.*

class VipsListAdapter : RecyclerView.Adapter<VipsListAdapter.Holder>() {

    private val items = ArrayList<VipVM>()

//    internal fun VipsActivityAdapter(activity: VipsActivity): ??? {
//        this.activity = activity
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater
                .from(parent.context).inflate(R.layout.vip_item, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal fun addOrUpdateItem(vipVM: VipVM) {
        val index = items.indexOf(vipVM)
        if (index == -1) {
            items.add(vipVM)
            notifyItemInserted(items.size - 1)
        } else {
            items[index] = vipVM
            notifyItemChanged(index)
        }
    }

    internal fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(vipVM: VipVM) {
            itemView.vip_item_name_text_view.text = vipVM.name
            itemView.vip_item_profession_text_view.text = vipVM.profession
            //     PicassoFilteredLoader.load(activity, vipVM.getPhotoUrl(), imageView)
            itemView.vip_item_ripple_layout.setOnClickListener { }
        }
    }
}