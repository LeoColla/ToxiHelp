package br.com.local.toxihelp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import androidx.core.text.HtmlCompat
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

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar_referencias)
        setSupportActionBar(toolbar)

        // Ativa o botão de "Home" (que por padrão é o voltar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Simula o voltar do sistema
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val textViewReferencias = findViewById<TextView>(R.id.texto_Referencias)

        try {
            val textoCompleto = assets.open("referencias.txt").bufferedReader().use { it.readText() }

            textViewReferencias.text = formataTexto(textoCompleto)
        } catch (e: Exception) {
            Log.e("Referencias", "Erro ao carregar as referencias: " + e.message)
            textViewReferencias.text = getString(R.string.referencias_erro)
        }



    }

    private fun formataTexto(texto : String) : CharSequence{
        val textoProcessado = texto.replace("\n", "<br>")

        val textoFormatado = HtmlCompat.fromHtml(
            textoProcessado,
            HtmlCompat.FROM_HTML_MODE_LEGACY)

        return textoFormatado
    }
}