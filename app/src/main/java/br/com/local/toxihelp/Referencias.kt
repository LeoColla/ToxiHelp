package br.com.local.toxihelp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView

class Referencias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_referencias)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_referencias)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textViewReferencias = findViewById<TextView>(R.id.texto_Referencias)

        try {
            val textoCompleto = assets.open("referencias.txt").bufferedReader().use { it.readText() }

            textViewReferencias.text = textoCompleto
        } catch (e: Exception) {
            Log.e("Referencias", "Erro ao carregar as referencias: " + e.message)
            textViewReferencias.text = getString(R.string.referencias_erro)
        }

    }
}