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

class CategoriaIntro : AppCompatActivity() {
    private val reposit by lazy {
        (application as ToxiHelpApplication).repository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categoria_intro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nomeCategoria = intent.getStringExtra("NOME_CATEGORIA")

        // validacao de nulo
        if (nomeCategoria == null) {
            Log.e("CategoriaIntro", "Nome da categoria não foi passado no Intent.")
            Toast.makeText(this, "Erro: Categoria não especificada.", Toast.LENGTH_LONG).show()
            finish()
            return // Para a execução do onCreate aqui
        }

        lifecycleScope.launch {
            val categoria = reposit.getCategoriaPorNome(nomeCategoria)

            if (categoria != null){
                val tituloTextView: TextView = findViewById(R.id.titulo_categoria_intro)
                val textoTextView: TextView = findViewById(R.id.texto_categoria_intro)

                tituloTextView.text = categoria.nome
                textoTextView.text = categoria.intro
            }
        }


    }
}