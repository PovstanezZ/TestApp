package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Firebase Authentication
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Привязка UI-элементов
        val userEmail: EditText = findViewById(R.id.email_text)
        val userPass: EditText = findViewById(R.id.pass_text)
        val button: Button = findViewById(R.id.button_reg)
        val login_page_button: TextView = findViewById(R.id.login_auth_text)

        login_page_button.setOnClickListener{
            val intent = Intent (this,login_page::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены, долбаёб >.<", Toast.LENGTH_LONG).show()
            }
            else {
                // Регистрация пользователя через Firebase
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                            val intent = Intent (this,login_page::class.java)
                            startActivity(intent)
                        } else {
                            val errorMessage = task.exception?.message ?: "Неизвестная ошибка"
                            Toast.makeText(this, "Ошибка регистрации: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}
