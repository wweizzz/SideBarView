package com.sceneconsole.arteffect.lib.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sceneconsole.arteffect.lib.imageloader.base.IImageLoader
import com.sceneconsole.arteffect.lib.imageloader.glide.module.GlideApp
import java.io.File
import java.util.concurrent.ExecutionException


object ImageLoader : IImageLoader {

    override fun pauseRequests(context: Context?) {
        context?.let {
            GlideApp.with(it)
                .pauseRequests()
        }
    }

    override fun resumeRequests(context: Context?) {
        context?.let {
            GlideApp.with(it)
                .pauseRequests()
        }
    }

    override fun ImageView.clear(context: Context?) {
        context?.let {
            GlideApp.with(it)
                .clear(this)
        }
    }

    override fun ImageView.loadImage(context: Context?, resourceId: Int) {
        context?.let {
            GlideApp.with(it)
                .load(resourceId)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //禁用缓存功能
                    //.skipMemoryCache(true) //禁用内存缓存)
                )
                .into(this)
        }
    }

    override fun ImageView.loadImageRound(
        context: Context?, resourceId: Int
    ) {
        context?.let {
            GlideApp.with(it)
                .load(resourceId)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //禁用缓存功能
                    //.skipMemoryCache(true) //禁用内存缓存)
                )
                .transform(CircleCrop())
                .into(this)
        }
    }

    override fun ImageView.loadImageRadius(
        context: Context?, resourceId: Int, radius: Int
    ) {
        context?.let {
            GlideApp.with(it)
                .load(resourceId)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //禁用缓存功能
                    //.skipMemoryCache(true) //禁用内存缓存)
                )
                .transform(CenterCrop(), RoundedCorners(radius))
                .into(this)
        }
    }

    override fun ImageView.loadImage(context: Context?, uri: Uri) {
        context?.let {
            GlideApp.with(it)
                .load(uri)
                .into(this)
        }
    }

    override fun ImageView.loadImageRound(context: Context?, uri: Uri) {
        context?.let {
            GlideApp.with(it)
                .load(uri)
                .transform(CircleCrop())
                .into(this)
        }
    }

    override fun ImageView.loadImageRadius(context: Context?, uri: Uri, radius: Int) {
        context?.let {
            GlideApp.with(it)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(radius))
                .into(this)
        }
    }

    override fun ImageView.loadImage(context: Context?, file: File) {
        context?.let {
            GlideApp.with(it)
                .load(file)
                .into(this)
        }
    }

    override fun ImageView.loadImageRound(context: Context?, file: File) {
        context?.let {
            GlideApp.with(it)
                .load(file)
                .transform(CircleCrop())
                .into(this)
        }
    }

    override fun ImageView.loadImageRadius(context: Context?, file: File, radius: Int) {
        context?.let {
            GlideApp.with(it)
                .load(file)
                .transform(CenterCrop(), RoundedCorners(radius))
                .into(this)
        }
    }

    override fun ImageView.loadImage(
        context: Context?, url: String?, errorResId: Int
    ) {
        context?.let {
            GlideApp.with(it)
                .load(url)
                .error(errorResId)
                .into(this)
        }
    }

    override fun ImageView.loadImageRound(
        context: Context?, url: String?, errorResId: Int
    ) {
        context?.let {
            GlideApp.with(it)
                .load(url)
                .error(errorResId)
                .transform(CircleCrop())
                .into(this)
        }
    }

    override fun ImageView.loadImageRadius(
        context: Context?, url: String?, radius: Int, errorResId: Int
    ) {
        context?.let {
            GlideApp.with(it)
                .load(url)
                .error(errorResId)
                .transform(CenterCrop(), RoundedCorners(radius))
                .into(this)
        }
    }

    override fun ImageView.loadImage(
        context: Context?, url: String?, options: RequestOptions?, errorResId: Int
    ) {
        context?.let {
            options?.let { options ->
                GlideApp.with(it)
                    .load(url)
                    .error(errorResId)
                    .apply(options)
                    .into(this)
            } ?: {
                GlideApp.with(it)
                    .load(url)
                    .error(errorResId)
                    .into(this)
            }
        }
    }

    override fun ImageView.loadGif(context: Context?, resourceId: Int) {
        context?.let {
            GlideApp.with(it)
                .asGif()
                .load(resourceId)
                .into(this)
        }
    }

    override fun ImageView.loadGif(context: Context?, url: String?) {
        context?.let {
            GlideApp.with(it)
                .asGif()
                .load(url)
                .into(this)
        }
    }

    override fun getImageDrawable(context: Context?, url: String?): Drawable? {
        if (TextUtils.isEmpty(url)) {
            return null
        }
        context?.let {
            try {
                return GlideApp.with(it)
                    .load(url)
                    .submit()
                    .get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun getImageBitmap(context: Context?, url: String?): Bitmap? {
        if (TextUtils.isEmpty(url)) {
            return null
        }
        context?.let {
            try {
                return GlideApp.with(it)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        return null
    }
}