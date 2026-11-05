package com.example.tempmail // تأكد إن اسم الباكدج عندك مطابق لمشروعك

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private var currentEmail: String = "" // متغير لحفظ الإيميل الحالي

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.1secmail.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(TempMailApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailTextView = findViewById(R.id.tvEmail)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val btnCopy = findViewById<Button>(R.id.btnCopy)

        // توليد إيميل جديد عند الضغط
        btnGenerate.setOnClickListener {
            fetchNewEmail()
        }

        // نسخ الإيميل عند الضغط على زر النسخ
        btnCopy.setOnClickListener {
            if (currentEmail.isNotEmpty()) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Email", currentEmail)
                clipboard.setPrimaryClip(clip)
                
                // رسالة تنبيه بإن الإيميل اتنسخ
                Toast.makeText(this, "تم نسخ البريد بنجاح!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "لا يوجد بريد لنسخه!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchNewEmail() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val emails = api.getRandomEmail()
                if (emails.isNotEmpty()) {
                    currentEmail = emails[0] // حفظ الإيميل في المتغير
                    withContext(Dispatchers.Main) {
                        emailTextView.text = currentEmail
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}