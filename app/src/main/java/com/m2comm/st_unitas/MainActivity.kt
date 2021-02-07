package com.m2comm.st_unitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), View.OnClickListener  {

    private val TAG: String = MainActivity::class.java.simpleName;
    private var mImageList = arrayListOf<ImgDTO>()
    val mKaKaoImgApi: KakaoImgService = KakaoImgService.create()
    var mAdapter: MyRecycleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton.setOnClickListener(this)

        mAdapter = MyRecycleAdapter(this, mImageList)
        mRecyclerview.adapter = mAdapter


    }

    private fun getKeyWord(query: String) {
        mImageList.clear()
        mKaKaoImgApi.requestList(query = query).enqueue(object : Callback<ImggDTO> {
            override fun onResponse(
                call: Call<ImggDTO>,
                response: Response<ImggDTO>
            ) {
                Log.d(TAG, "OKOOK=${response.body()!!.documents.size}")
                if (response.body() != null) {
                    for (row in response.body()!!.documents) {
                        mImageList.add(
                            ImgDTO(
                                row.collection,
                                row.datetime,
                                row.display_sitename,
                                row.doc_url,
                                row.height,
                                row.image_url,
                                row.thumbnail_url,
                                row.width
                            )
                        )
                    }
                    mAdapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ImggDTO>, t: Throwable) {
                Log.d(TAG, "Error=${t.toString()}")
            }
        })
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.mButton -> if (mEditText.text.toString() == "") {
                    Toast.makeText(
                        this, "검색어를 입력해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    this.getKeyWord(mEditText.text.toString())
                }
                else -> ""
            }
        }


    }
}

