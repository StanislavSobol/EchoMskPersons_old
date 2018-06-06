package com.gmail.echomskfan.persons.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.gmail.echomskfan.persons.MApplication
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

object PicassoFilteredLoader {

    fun load(context: Context, url: String, imageView: ImageView) {
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                Log.d("SSS", "PicassoFilteredLoader :: onBitmapFailed url = $url")
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    //                    imageView.setImageBitmap(BitmapFilters.setFilter(bitmap))
                    imageView.setImageBitmap(bitmap)
                }
            }

        }

        imageView.tag = target

        val requestCreator = Picasso.with(context).load(url)
        if (!MApplication.isOnlineWithToast(false)) {
            requestCreator.networkPolicy(NetworkPolicy.OFFLINE)
        }
        requestCreator.into(target)
    }

}