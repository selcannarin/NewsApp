package com.example.newsapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.newsapp.R

fun ImageView.loadUrl(url: String) {
    Glide.with(this)
        .load(url)
        .fitCenter()
        .transform(RoundedCorners(50))
        .placeholder(R.drawable.img_placeholder)
        .into(this)
}