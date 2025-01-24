package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Namer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_namer)

        val nameInput = findViewById<EditText>(R.id.name_text_card)
        val saveButton = findViewById<Button>(R.id.name_card_button)

        saveButton.setOnClickListener {
            val cardName = nameInput.text.toString().trim()
            if (cardName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("cardName", cardName)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
