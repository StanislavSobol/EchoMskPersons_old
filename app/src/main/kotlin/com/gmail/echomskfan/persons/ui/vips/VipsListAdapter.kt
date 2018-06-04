package com.gmail.echomskfan.persons.ui.vips

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.VipVM
import com.gmail.echomskfan.persons.utils.PicassoFilteredLoader
import com.gmail.echomskfan.persons.utils.fromIoToMain
import kotlinx.android.synthetic.main.vip_item.view.*
import java.util.*

class VipsListAdapter(val context: Context, val presenter: VipsPresenter) : RecyclerView.Adapter<VipsListAdapter.Holder>() {

    private val items = ArrayList<VipVM>()

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

    internal fun addItems(vips: List<VipVM>) {
        items.clear()
        items.addAll(vips)
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(vipVM: VipVM) {
            itemView.vipItemNameTextView.text = vipVM.name
            itemView.vipItemProfessionTextView.text = vipVM.profession
            PicassoFilteredLoader.load(context, vipVM.photoUrl, itemView.vipItemImageView)
            itemView.vipItemInfoTextView.text = vipVM.info

            itemView.vipItemNotificationImageView.setImageResource(
                    if (vipVM.notification) R.drawable.ic_baseline_notifications_24px
                    else R.drawable.ic_baseline_notifications_none_24px
            )
            itemView.vipItemNotificationImageView.setOnClickListener {
                presenter.setNotificationForItem(vipVM).fromIoToMain().subscribe {
                    vipVM.notification = !vipVM.notification
                    notifyDataSetChanged()
//                    if (vipVM.notification) R.drawable.ic_baseline_notifications_24px
//                    else R.drawable.ic_baseline_notifications_none_24px
                }
            }

            itemView.vipItemFavImageView.setImageResource(
                    if (vipVM.fav) R.drawable.ic_baseline_favorite_24px
                    else R.drawable.ic_baseline_favorite_border_24px
            )


            //    itemView.vip_item_ripple_layout.setOnClickListener { }
        }
    }
}