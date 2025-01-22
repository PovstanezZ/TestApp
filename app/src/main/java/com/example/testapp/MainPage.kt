package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    // Инициализация сканера
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // Результат сканирования (штрих-код или QR-код)
            val scannedCode = result.contents
            Toast.makeText(this, "Сканировано: $scannedCode", Toast.LENGTH_SHORT).show()
            // Здесь можно сохранить карту в базу данных
        } else {
            Toast.makeText(this, "Сканирование отменено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)
        auth = FirebaseAuth.getInstance()

        // Обработка системных отступов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Кнопка "Выход"
        val exitButton: TextView = findViewById(R.id.exit_button)
        exitButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Кнопка для добавления новой карты
        val addCardButton: Button = findViewById(R.id.add_new_card)
        addCardButton.setOnClickListener {
            startBarcodeScanner()
        }
    }

    private fun startBarcodeScanner() {
        val options = ScanOptions()
        options.setPrompt("Сканируйте штрих-код или QR-код")
        options.setBeepEnabled(true)            // Включить звуковой сигнал
        options.setOrientationLocked(true)      // Заблокировать вертикальную ориентацию
        options.setCaptureActivity(AnyOrientationCaptureActivity::class.java)
        barcodeLauncher.launch(options)         // Запустить сканер
    }

}

