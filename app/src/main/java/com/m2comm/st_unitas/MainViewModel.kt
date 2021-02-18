package com.m2comm.st_unitas

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {

    public var title : MutableLiveData<String> = MutableLiveData()


//    val handler : Handler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(inputMessage: Message) {
//            if (mEditText.text.toString() != "") {
//                mImageList.clear()
//                mAdapter.notifyDataSetChanged()
//                getKeyWord(mEditText.text.toString())
//                hideKeyboard()
//            }
//        }
//    }


}