package com.m2comm.st_unitas

class ImgDTO ( val collection : String , val datetime : String ,
               val display_sitename : String , val doc_url : String ,
               val height : Int , val image_url : String , val thumbnail_url : String , val width : Int)

class ImggDTO (val documents : ArrayList<ImgDTO>)