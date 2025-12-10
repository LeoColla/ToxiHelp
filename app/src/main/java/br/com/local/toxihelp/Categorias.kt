package br.com.local.toxihelp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.local.toxihelp.data.CategoriaAdapter
import kotlinx.coroutines.launch

class Categorias : AppCompatActivity() {
    private val categoriaRepository by lazy {
        (application as ToxiHelpApplication).repository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categorias)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_categorias)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rv = findViewById<RecyclerView>(R.id.rv_categorias)
        rv.layoutManager = LinearLayoutManager(this)

        // Criamos o adapter uma vez, com uma lista vazia.
        val adapter = CategoriaAdapter { categoria ->
            Log.d("CategoriasActivity", "Clique no item: ${categoria.nome}")
            Toast.makeText(
                this@Categorias,
                "Categoria clicada: ${categoria.nome}",
                Toast.LENGTH_SHORT
            ).show()
        }
        rv.adapter = adapter

        // Lançamos uma corrotina que vai "escutar" as atualizações do banco.
        lifecycleScope.launch {
            Log.d("CategoriasActivity", "Iniciando a coleta do Flow de categorias.")

            // o bloco será executado imediatamente com os dados atuais (lista vazia)
            // e depois, novamente, quando o DataLoader terminar e o banco for atualizado.
            categoriaRepository.getCategorias().collect { categorias ->
                Log.d("CategoriasActivity", "Flow coletado. Novo número de categorias: ${categorias.size}")

                // Atualizamos a lista de dados no adapter
                adapter.submitList(categorias)
            }
        }
    }
}