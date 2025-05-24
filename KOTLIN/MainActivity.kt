package com.example.helloword

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val layout= LinearLayout(this).apply {
            orientation= LinearLayout.VERTICAL
            gravity= Gravity.CENTER
            layoutParams= LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
         val txt= TextView(this).apply {
             text="Hello World"
             textSize=32f
             gravity= Gravity.CENTER
         }
        layout.addView(txt)
        setContentView(layout)

    }
}