package com.example.testapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRCodeAdapter(private val dataList: List<Pair<String, String>>) :
    RecyclerView.Adapter<QRCodeAdapter.QRCodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QRCodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.qr_code_item, parent, false)
        return QRCodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: QRCodeViewHolder, position: Int) {
        val (name, qrData) = dataList[position] // Разделяем имя карты и данные QR-кода

        // Устанавливаем имя карты
        holder.nameOfQR.text = name

        // Устанавливаем данные QR-кода
        holder.dataOfQR.text = qrData

        // Генерируем QR-код и устанавливаем в ImageView
        val qrBitmap = generateQRCode(qrData)
        if (qrBitmap != null) {
            holder.qrCodeImageView.setImageBitmap(qrBitmap)
        } else {
            holder.qrCodeImageView.setImageResource(R.drawable.placeholder) // Укажите ресурс-заглушку
        }
    }

    override fun getItemCount(): Int = dataList.size

    private fun generateQRCode(data: String): Bitmap? {
        return try {
            val barcodeEncoder = BarcodeEncoder()
            val bitMatrix: BitMatrix =
                com.google.zxing.qrcode.QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 400, 400)
            barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    class QRCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfQR: TextView = itemView.findViewById(R.id.NameOfQR)
        val dataOfQR: TextView = itemView.findViewById(R.id.dataOfQR)
        val qrCodeImageView: ImageView = itemView.findViewById(R.id.qrCodeImageView)
    }
}

