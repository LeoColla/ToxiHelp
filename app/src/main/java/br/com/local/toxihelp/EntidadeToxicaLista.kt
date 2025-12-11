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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.local.toxihelp.data.EntidadeToxicaAdapter
import kotlinx.coroutines.launch

class EntidadeToxicaLista : AppCompatActivity() {
    private val reposit by lazy {
        (application as ToxiHelpApplication).repository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entidade_toxica_lista)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_entidade_toxica_lista)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // nome da categoria a partir do Intent
        val nomeCategoria = intent.getStringExtra("NOME_CATEGORIA")

        // validacao de nulo
        if (nomeCategoria == null) {
            Log.e("EntidadeActivity", "Nome da categoria não foi passado no Intent.")
            Toast.makeText(this, "Erro: Categoria não especificada.", Toast.LENGTH_LONG).show()
            finish()
            return // Para a execução do onCreate aqui
        }

        val titulo: TextView = this.findViewById(R.id.titulo_entidade_toxica_lista)
        titulo.text = nomeCategoria


        val rv = findViewById<RecyclerView>(R.id.rv_entidade_toxica_lista)
        rv.layoutManager = LinearLayoutManager(this)

        // Criamos o adapter uma vez, com uma lista vazia.
        val adapter = EntidadeToxicaAdapter { entidade ->
            Log.d("EntidadeActivity", "Clique no item: ${entidade.nomePopular}")
            Toast.makeText(
                this@EntidadeToxicaLista,
                "Entidade clicada: ${entidade.nomePopular}",
                Toast.LENGTH_SHORT
            ).show()
        }
        rv.adapter = adapter

        // Lançamos uma corrotina que vai "escutar" as atualizações do banco.
        lifecycleScope.launch {
            Log.d("EntidadeActivity", "Iniciando a coleta do Flow de entidades.")

            reposit.getEntidadesResumoPorCategoria(nomeCategoria).collect { entidade ->
                Log.d("EntidadeActivity", "Flow coletado. Novo número de entidades: ${entidade.size}")

                // Atualizamos a lista de dados no adapter
                adapter.submitList(entidade)
            }
        }
    }
}