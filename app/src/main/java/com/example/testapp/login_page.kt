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

class login_page : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailField: EditText = findViewById(R.id.login_text_auth)
        val passwordField: EditText = findViewById(R.id.pass_text_auth)
        val loginButton: Button = findViewById(R.id.button_auth)
        val reg_button: TextView = findViewById(R.id.reg_text)

        reg_button.setOnClickListener{
            val intent = Intent (this,MainActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Авторизация успешна!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainPage::class.java))
                            finish()
                        } else {
                            val errorMessage = task.exception?.message ?: "Ошибка входа"
                            Toast.makeText(this, "Ошибка входа: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}
