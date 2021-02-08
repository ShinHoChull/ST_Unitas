package com.m2comm.st_unitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var mAdapter: MyRecycleAdapter
    var mKeyword = ""

    private var mIsEnd = true // 다음 페이지 유무 있으면 false
    private var page = 0       // 현재 페이지

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton.setOnClickListener(this)

        mAdapter = MyRecycleAdapter(this, mImageList)
        mRecyclerview.adapter = mAdapter

        mRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = mRecyclerview.layoutManager

                // hasNextPage() -> 다음 페이지가 있는 경우 false
                if (!hasNextPage()) {
                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                    Log.d(TAG , "InData=${layoutManager.itemCount}")
                    Log.d(TAG , "InData=${lastVisibleItem + 2}")
                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 2개 모자란 경우, 데이터를 loadMore 한다
                    if (layoutManager.itemCount <= lastVisibleItem + 2) {
                        getKeyWord(mKeyword)
                        setHasNextPage(true)
                    }
                }
            }
        })
    }

    private fun getKeyWord(query: String) {
        mKeyword = query
        mAdapter.setLoadingView(true)

        val handler = android.os.Handler()
        handler.postDelayed({
            mKaKaoImgApi.requestList(query = query,page = getPage()).enqueue(object : Callback<ParentImgDTO> {
                override fun onResponse(
                    call: Call<ParentImgDTO>,
                    response: Response<ParentImgDTO>
                ) {
                    Log.d(TAG, "OKOOK=${response.body()!!.meta.pageable_count}")
                    setHasNextPage(response.body()!!.meta.is_end)
                    if (response.body() != null && response.isSuccessful ) {
                        mAdapter.setLoadingView(false)
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
                        mAdapter.run {
                            setLoadingView(false)
                            addPosts()
                        }
                    }
                }

                override fun onFailure(call: Call<ParentImgDTO>, t: Throwable) {
                    Log.d(TAG, "Error=${t.toString()}")
                }
            })
        },1000)

    }

    private fun getPage(): Int {
        page++
        return page
    }

    private fun hasNextPage(): Boolean {
        return mIsEnd
    }

    private fun setHasNextPage(b: Boolean) {
        mIsEnd = b
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

