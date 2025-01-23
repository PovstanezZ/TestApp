package com.example.testapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRCodeAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<QRCodeAdapter.QRCodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QRCodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.qr_code_item, parent, false)
        return QRCodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: QRCodeViewHolder, position: Int) {
        val data = dataList[position]
        val qrBitmap = generateQRCode(data)
        if (qrBitmap != null) {
            holder.qrCodeImageView.setImageBitmap(qrBitmap)
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
        val qrCodeImageView: ImageView = itemView.findViewById(R.id.qrCodeImageView)
    }
}
