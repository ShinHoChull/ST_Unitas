package com.m2comm.st_unitas

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/*
*  WebChromeClient 같은경우는 재정의를 해주지 않으면 WebView URL이 그대로 노출이 된다.
* */
class Chromeclient_Test(activity: Activity, var context: Context, webView: WebView) : WebChromeClient() {
    var activity: Activity
    var subContext: Context
    var webView: WebView
    private var doubleCheckedPopUp = 0

    //File Upload를 위한 ...
    var filePathCallbackNormal: ValueCallback<Uri>? = null
    var filePathCallbackLollipop: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null

    // For Android Version < 3.0
    fun openFileChooser(uploadMsg: ValueCallback<Uri>?) {
        //System.out.println("WebViewActivity OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU), n=1");
        mUploadMessage = uploadMsg
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = TYPE_IMAGE
        activity.startActivityForResult(intent, INPUT_FILE_REQUEST_CODE)
    }
    // For 3.0 <= Android Version < 4.1
    fun openFileChooser(uploadMsg: ValueCallback<Uri>?, acceptType: String?) {
        //System.out.println("WebViewActivity 3<A<4.1, OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU,aT), n=2");
        openFileChooser(uploadMsg, acceptType, "")
    }

    // For 4.1 <= Android Version < 5.0
    fun openFileChooser(uploadFile: ValueCallback<Uri>?, acceptType: String?, capture: String?) {
        mUploadMessage = uploadFile
        imageChooser()
    }

    // For Android Version 5.0+
    // Ref: https://github.com/GoogleChrome/chromium-webview-samples/blob/master/input-file-example/app/src/main/java/inputfilesample/android/chrome/google/com/inputfilesample/MainFragment.java
    override fun onShowFileChooser(webView: WebView,
                                   filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
        println("WebViewActivity A>5, OS Version : " + Build.VERSION.SDK_INT + "\t onSFC(WV,VCUB,FCP), n=3")
        if (mFilePathCallback != null) {
            mFilePathCallback!!.onReceiveValue(null)
        }
        mFilePathCallback = filePathCallback
        imageChooser()
        return true
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
    }

    private fun imageChooser() {
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(context.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Log.e(javaClass.name, "Unable to create Image File", ex)
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile))
            } else {
                takePictureIntent = null
            }
        }
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = TYPE_IMAGE
        val intentArray: Array<Intent?>
        intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        activity.startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
    }

    override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
        Log.d("windowOpen", "open Url =$view")
        webView.removeAllViews()
        val childView = WebView(activity)

        return true
    }

    override fun onCloseWindow(window: WebView) {
        super.onCloseWindow(window)

        //매개변수로 받은 webview 객체를 removeAllViews 시킴으로써 클로즈윈도우할때 같이 삭제를 해준다.
        webView.removeAllViews()
        doubleCheckedPopUp = 0

    }

    //dialogWindow cusomize 가능.
    override fun onJsAlert(view: WebView, url: String, message: String,
                           result: JsResult): Boolean {
        // TODO Auto-generated method stub
        AlertDialog.Builder(view.context).setTitle("Alert")
                .setMessage(message)
                .setPositiveButton(R.string.ok
                ) { dialog, which -> result.confirm() }.setCancelable(false)
                .create()
                .show()
        return true
    }

    override fun onJsConfirm(view: WebView, url: String, message: String,
                             result: JsResult): Boolean {
        // TODO Auto-generated method stub
        AlertDialog.Builder(view.context)
                .setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("YES"
                ) { dialog, which -> // TODO Auto-generated method stub
                    result.confirm()
                }
                .setNegativeButton("NO"
                ) { dialog, which -> // TODO Auto-generated method stub
                    result.cancel()
                }
                .setCancelable(false)
                .create()
                .show()
        return true
    }

    companion object {
        private const val TYPE_IMAGE = "image/*"
        private const val INPUT_FILE_REQUEST_CODE = 1
    }

    init {
        subContext = activity
        this.webView = webView
        this.activity = activity
    }
}