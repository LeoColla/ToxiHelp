package br.com.local.toxihelp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Introducao : AppCompatActivity() {
    private val reposit by lazy {
        (application as ToxiHelpApplication).repository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(R.layout.activity_introducao)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_introducao)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val introInfo = intent.getStringExtra("INTRO")

        // validacao de nulo
        when (introInfo) {
            null -> {
                Log.e("Introducao", "Intro Info não foi passado no Intent.")
                Toast.makeText(this, "Erro: Introdução não especificada.", Toast.LENGTH_LONG).show()
                finish()
                return // Para a execução do onCreate aqui
            }
            "introGeral" -> {
                lifecycleScope.launch {
                    val tituloTextView: TextView = findViewById(R.id.titulo_intro)
                    val textoTextView: TextView = findViewById(R.id.texto_intro)

                    tituloTextView.text = getString(R.string.intro_geral_nome)
                    textoTextView.text = getString(R.string.intro_geral_intro)
                }
            }
            else -> {
                lifecycleScope.launch {
                    val categoria = reposit.getCategoriaPorNome(introInfo)

                    if (categoria != null){
                        val tituloTextView: TextView = findViewById(R.id.titulo_intro)
                        val textoTextView: TextView = findViewById(R.id.texto_intro)

                        tituloTextView.text = categoria.nome
                        textoTextView.text = categoria.intro
                    }
                }
            }
        }


    }
}