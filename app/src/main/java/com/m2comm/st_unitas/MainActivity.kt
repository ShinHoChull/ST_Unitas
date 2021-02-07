package com.m2comm.st_unitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call


class MainActivity : AppCompatActivity() ,View.OnClickListener{

    private val TAG: String = MainActivity::class.java.simpleName;
    private var mImageList  = arrayListOf<ImgDTO>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val service = Common.RetrofitClient.getInstance()
        service.create(KakaoImgService::class.java)





        mImageList.add(ImgDTO("cafe","2021-01-14T23:54:34.000+09:00",
        "Daum카페","http://cafe.daum.net/bakamyon/O3fa/5462",
                246,"http://cfs9.planet.daum.net/upload_control/pcp_download.php?fhandle=bDNRQUBmczkucGxhbmV0LmRhdW0ubmV0Oi80MzE3NzUyLzAvMi5qcGcudGh1bWI=&filename=%EB%8B%B9%EC%8B%A0%EC%9D%84%EC%82%AC%EB%9E%91%ED%95%98%EB%8A%94%EC%82%AC%EB%9E%8C.jpg",
                "https://search2.kakaocdn.net/argon/130x130_85_c/4XrbEcfQqcv",400))


        mButton.setOnClickListener(this)

        val mAdapter = MyRecycleAdapter(this , mImageList)
        mRecyclerview.adapter = mAdapter



    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id) {
                R.id.mButton-> if ( mEditText.text.toString() == "" ){
                    Toast.makeText(this , "검색어를 입력해주세요.${Common.API_KEY}",
                        Toast.LENGTH_SHORT).show()
                } else {

                }
                else ->""
            }
        }



    }
}
