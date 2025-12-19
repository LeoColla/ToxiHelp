package br.com.local.toxihelp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.local.toxihelp.data.ElementosAdapter
import kotlinx.coroutines.launch
import androidx.core.content.edit

class ElementoLista : AppCompatActivity() {
    private val reposit by lazy {
        (application as ToxiHelpApplication).repository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_elemento_lista)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_elemento_lista)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // nome da categoria a partir do Intent
        val nomeCategoria = intent.getStringExtra("NOME_CATEGORIA")

        // validacao de nulo
        if (nomeCategoria == null) {
            Log.e("ElementoActivity", "Nome da categoria não foi passado no Intent.")
            Toast.makeText(this, "Erro: Categoria não especificada.", Toast.LENGTH_LONG).show()
            finish()
            return // Para a execução do onCreate aqui
        }

        val prefs = getSharedPreferences("toxihelp_prefs", MODE_PRIVATE)
        val chaveCategoriaVista = "vista_intro_$nomeCategoria"

        val vistaIntro = prefs.getBoolean(chaveCategoriaVista, false)
        if (!vistaIntro) {
            val intent = Intent(this@ElementoLista, CategoriaIntro::class.java).apply {
                putExtra("NOME_CATEGORIA", nomeCategoria)
            }
            startActivity(intent)
            prefs.edit { putBoolean(chaveCategoriaVista, true) }
        }



        // torna o botao de intro clicavel
        // para abrir a tela de intro da categoria
        val introButton: TextView = this.findViewById(R.id.info_categoria)
        introButton.setOnClickListener {
            val intent = Intent(this@ElementoLista, CategoriaIntro::class.java).apply {
                putExtra("NOME_CATEGORIA", nomeCategoria)
            }
            startActivity(intent)
        }



        val titulo: TextView = this.findViewById(R.id.titulo_elemento_resumo_lista)
        titulo.text = nomeCategoria

        val rv = findViewById<RecyclerView>(R.id.rv_elemento_resumo_lista)
        rv.layoutManager = LinearLayoutManager(this)

        // Criamos o adapter uma vez, com uma lista vazia.
        val adapter = ElementosAdapter { elemento ->
            Log.d("ElementoActivity", "Clique no item: ${elemento.nomePopular}")

            val intent = Intent(this@ElementoLista, ElementoDetalhe::class.java).apply {
                putExtra("NOME_POPULAR", elemento.nomePopular)
            }
            startActivity(intent)
        }
        rv.adapter = adapter

        // Lançamos uma corrotina que vai "escutar" as atualizações do banco.
        lifecycleScope.launch {
            Log.d("ElementoActivity", "Iniciando a coleta do Flow de Elementos.")

            reposit.getElementosResumoPorCategoria(nomeCategoria).collect { elementos ->
                Log.d("ElementoActivity", "Flow coletado. Novo número de Elementos: ${elementos.size}")

                val elementosOrdenados = elementos.sortedBy { it.nomePopular }
                // Atualizamos a lista de dados no adapter
                adapter.submitList(elementosOrdenados)
            }
        }
    }
}