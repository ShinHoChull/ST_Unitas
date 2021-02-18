package com.m2comm.st_unitas.moden_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.m2comm.st_unitas.R
import kotlinx.android.synthetic.main.activity_memo.*

class MemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "todo-db"
        ).allowMainThreadQueries()
            .build()

        result_text.text = db.todoDao().getAll().toString()


        add_button.setOnClickListener {
//            db.todoDao().insert(Todo(todo_edit.text.toString()))
//            result_text.text = db.todoDao().getAll().toString()
        }

    }

}