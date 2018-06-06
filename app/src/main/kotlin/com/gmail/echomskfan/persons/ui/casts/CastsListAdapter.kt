package com.gmail.echomskfan.persons.ui.casts

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.utils.StringUtils
import com.gmail.echomskfan.persons.utils.makeGone
import com.gmail.echomskfan.persons.utils.makeVisible
import kotlinx.android.synthetic.main.item_cast.view.*

class CastsListAdapter(private val context: Context, private val presenter: CastsPresenter) : RecyclerView.Adapter<CastsListAdapter.Holder>() {

    private var items = mutableListOf<CastVM>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater
                .from(parent.context).inflate(R.layout.item_cast, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setItem(items[position])
    }

    fun addItems(newItems: List<CastVM>) {
        newItems.forEach {
            val newItem = it
            items.find { newItem.fullTextURL == it.fullTextURL }?.let { items.remove(it) }
            items.add(newItem)
        }

        items = items.sortedBy { it.pageNum }.toMutableList()
        notifyDataSetChanged()
    }

    fun updateItem(item: CastVM) {
        items.find { item.fullTextURL == it.fullTextURL }?.fav = item.fav
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setItem(item: CastVM) {
            itemView.item_content_type_text_view.text = item.type
            itemView.item_content_date_text_view.text = item.formattedDate
            itemView.item_content_short_text_view.text = item.shortText

            if (!item.mp3Url.isEmpty()) {
                itemView.item_content_audio_duration_text_view.text = StringUtils.getAudioDuration(item.mp3Duration)
                itemView.item_content_audio_play_image_button.setOnClickListener {
                    // presenter.play !
                }

                itemView.item_content_audio_layout.makeVisible()
            } else {
                itemView.item_content_audio_layout.makeGone()
            }

            itemView.item_content_favorite_image_view.setImageResource(
                    if (item.fav) R.drawable.ic_baseline_favorite_24px
                    else R.drawable.ic_baseline_favorite_border_24px
            )

            itemView.item_content_favorite_image_view.setOnClickListener {
                presenter.itemFavIconClicked(item)
            }
        }
    }
}