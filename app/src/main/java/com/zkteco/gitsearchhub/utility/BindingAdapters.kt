package com.zkteco.gitsearchhub.utility


import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zkteco.gitsearchhub.R

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.drawable.profile_pic)
            .error(R.drawable.profile_pic)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(view)
    }

}
