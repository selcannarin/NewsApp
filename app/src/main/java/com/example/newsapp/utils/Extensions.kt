package com.example.newsapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun ImageView.loadUrl(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .transform(RoundedCorners(50))
        .into(this)
}