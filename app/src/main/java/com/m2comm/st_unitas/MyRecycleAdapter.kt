package com.m2comm.st_unitas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyRecycleAdapter(val mContext : Context, val mImgList : ArrayList<ImgDTO> )
    : RecyclerView.Adapter<MyRecycleAdapter.ViewHolder>() {

    companion object {
        private const val TYPE_POST = 0
        private const val TYPE_LOADING = 1
    }

    fun setPosts(posts: ArrayList<ImgDTO>) {
        this.mImgList.apply {
            clear()
            addAll(posts)
        }
        notifyDataSetChanged()
    }

    fun addPosts() {
        //this.mImgList.addAll(posts)
        notifyDataSetChanged()
    }

    fun setLoadingView(b: Boolean) {
        if (b) {
            android.os.Handler().post {

                this.mImgList.add(ImgDTO("","","","",-1,"","",-1))
                notifyItemInserted(mImgList.size - 1)
            }
        } else {
            if (this.mImgList[mImgList.size - 1].height == -1) {
                this.mImgList.removeAt(mImgList.size - 1)
                notifyItemRemoved(mImgList.size)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        when (viewType) {
            TYPE_POST -> {
                val inflatedView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.recycleview_item, parent, false)
                return ViewHolder(inflatedView)
            }
            else -> {
                val inflatedView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_loding, parent, false)
                return ViewHolder(inflatedView)
            }
        }

//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.recycleview_item , parent , false)
//        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_POST -> {
                val postViewHolder = holder as ViewHolder
                postViewHolder.bind(mImgList[position] , mContext)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (mImgList[position].height) {
            -1 -> TYPE_LOADING
            else -> TYPE_POST
        }
    }

    override fun getItemCount(): Int {
        return mImgList.size
    }

    inner class ViewHolder(itemView : View ) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.mImageView)
        fun bind(row : ImgDTO , context : Context) {

            val display =  context.resources.displayMetrics

            Glide.with(mContext).load(row.image_url).override(display.widthPixels / 2, display.widthPixels / 2).into(img)
        }
    }
}