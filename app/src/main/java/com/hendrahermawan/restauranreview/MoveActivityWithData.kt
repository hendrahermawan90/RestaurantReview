package com.hendrahermawan.restauranreview

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MoveActivityWithData : AppCompatActivity() {
    companion object {
        const val YOUR_NAME = "your_name"
        const val YOUR_NIM = "your nim"
        const val YOUR_CLASS = "your_class"
        const val YOUR_AGE = "your age"
        const val YOUR_EMAIL = "your_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_move_with_data)

        val tvDataReceived: TextView = findViewById(R.id.tv_data_received)


        val name = intent.getStringExtra(YOUR_NAME)
        val nim = intent.getIntExtra(YOUR_NIM,0)
        val clas = intent.getStringExtra(YOUR_CLASS)
        val age = intent.getIntExtra(YOUR_AGE,0)
        val email = intent.getStringExtra(YOUR_EMAIL)

        val text = "Nama : $name \nNIM : $nim \nKelas : $clas \nUsia : $age \nEmail : $email"

        tvDataReceived.text = text
    }
}