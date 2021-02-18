package com.m2comm.st_unitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity()  {

    private val TAG: String = MainActivity::class.java.simpleName;
    private var mImageList = arrayListOf<ImgDTO>()
    val mKaKaoImgApi: KakaoImgService = KakaoImgService.create()
    private lateinit var mAdapter: MyRecycleAdapter
    var mKeyword = ""
    var mTimer = Timer()

    private var mIsEnd = true // 다음 페이지 유무 있으면 false
    private var mPage = 0       // 현재 페이지

    private lateinit var mMainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = MyRecycleAdapter(this, mImageList)

        mMainViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mMainViewModel.title.observe(this , androidx.lifecycle.Observer {
            Log.d("Data=>",it)
        })


        mRecyclerview.adapter = mAdapter
        mRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = mRecyclerview.layoutManager

                // hasNextPage() -> 다음 페이지가 있는 경우 false
                if (!hasNextPage()) {
                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()

                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 2개 모자란 경우, 데이터를 loadMore 한다
                    if (layoutManager.itemCount <= lastVisibleItem + 2) {
                        getKeyWord(mKeyword)
                        setHasNextPage(true)
                    }
                }
            }
        })


        mEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                mTimer.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                mTimer = timer(period = 1000 , initialDelay = 1000) {
                    handler.obtainMessage().sendToTarget()
                    mTimer.cancel()
                }
            }
        })
    }

    val handler : Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage( inputMessage: Message ) {
            if ( mEditText.text.toString() != "" ) {
                mMainViewModel.title.value = mEditText.text.toString()

//                mMainViewModel.title = mEditText.text.toString()
//                mImageList.clear()
//                mAdapter.notifyDataSetChanged()
//                getKeyWord(mEditText.text.toString())
//                hideKeyboard()
            }
        }
    }

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
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
                    if ( response.body() != null && response.isSuccessful ) {
                        setHasNextPage(response.body()!!.meta.is_end)
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
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ParentImgDTO>, t: Throwable) {
                    Log.d(TAG, "Error=${t.toString()}")
                }
            })
        },1000)
    }

    private fun getPage(): Int {
        mPage++
        return mPage
    }

    private fun hasNextPage(): Boolean {
        return mIsEnd
    }

    private fun setHasNextPage(b: Boolean) {
        mIsEnd = b
    }



}

