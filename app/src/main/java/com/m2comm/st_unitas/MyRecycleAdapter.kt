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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_item , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mImgList[position] , mContext)
    }

    override fun getItemCount(): Int {
        return mImgList.size
    }

    inner class ViewHolder(itemView : View ) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.mImageView)
        fun bind(row : ImgDTO , context : Context) {
            Glide.with(mContext).load(row.thumbnail_url).into(img)
        }
    }
}