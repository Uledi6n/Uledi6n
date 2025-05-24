package com.example.myapplication

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Root vertical layout
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(16)
        }
        val tlview=TextView(this).apply{
            text="Uledi Calculator"
            textSize = 32f
            setTypeface(null, Typeface.BOLD)
            
        }
        layout.addView(tlview)

        // TextView at the top
        val txt = TextView(this).apply {
            text = "0"
            textSize = 32f
            gravity = Gravity.END
            setTypeface(null, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        layout.addView(txt)

        // Buttons
        val buttons = arrayOf(
            arrayOf("7", "8", "9", "/"),
            arrayOf("4", "5", "6", "*"),
            arrayOf("1", "2", "3", "-"),
            arrayOf("0", "C", "=", "+")
        )

        for (row in buttons) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            for (txtbtn in row) {
                val btn = Button(this).apply {
                    text = txtbtn
                    textSize = 24f
                    setOnClickListener {
                        txt.append(text)
                    }
                }

                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                params.setMargins(8, 8, 8, 8)
                rowLayout.addView(btn, params)
            }

            layout.addView(rowLayout)
        }

        setContentView(layout)
    }
}
