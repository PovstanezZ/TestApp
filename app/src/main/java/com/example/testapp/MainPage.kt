package com.example.testapp

import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val qrCodeList = mutableListOf<Pair<String, String>>() // Список для имени карты и QR-кода


    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            val scannedCode = result.contents
            Toast.makeText(this, "Сканировано: $scannedCode", Toast.LENGTH_SHORT).show()

            // Добавляем QR-код в список с пустым именем
            qrCodeList.add("" to scannedCode) // "" — имя по умолчанию
            recyclerView.adapter?.notifyDataSetChanged()

            // Открываем окно для задания имени карты
            openNamer()
        } else {
            Toast.makeText(this, "Сканирование отменено", Toast.LENGTH_SHORT).show()
        }
    }


    private val nameCardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val cardName = result.data?.getStringExtra("cardName") ?: "Без имени"
                if (qrCodeList.isNotEmpty()) {
                    val lastItem = qrCodeList.last()
                    qrCodeList[qrCodeList.size - 1] = cardName to lastItem.second
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = QRCodeAdapter(qrCodeList)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addCardButton: Button = findViewById(R.id.add_new_card)
        addCardButton.setOnClickListener {
            startBarcodeScanner()
        }

    }

    private fun startBarcodeScanner() {
        val options = ScanOptions()
        options.setPrompt("Сканируйте штрих-код или QR-код")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setCaptureActivity(AnyOrientationCaptureActivity::class.java)

        barcodeLauncher.launch(options)
    }

    private fun openNamer() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.activity_namer, null)
        builder.setView(dialogView)

        val nameInput = dialogView.findViewById<EditText>(R.id.name_text_card)
        val nameButton = dialogView.findViewById<Button>(R.id.name_card_button)

        val dialog = builder.create()

        nameButton.setOnClickListener {
            val cardName = nameInput.text.toString().trim()
            if (cardName.isNotEmpty()) {
                if (qrCodeList.isNotEmpty()) {
                    // Обновляем имя для последнего элемента
                    val lastItem = qrCodeList.last()
                    qrCodeList[qrCodeList.size - 1] = cardName to lastItem.second
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                dialog.dismiss() // Закрываем диалог
            } else {
                Toast.makeText(this, "Введите имя карты", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }


}
