package br.com.local.toxihelp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.local.toxihelp.domain.ICategoria
import br.com.local.toxihelp.domain.IItem
import br.com.local.toxihelp.domain.Agrotoxico
import br.com.local.toxihelp.domain.AnimalPeconhento
import br.com.local.toxihelp.domain.Cosmetico
import br.com.local.toxihelp.domain.PlantaToxica
import br.com.local.toxihelp.domain.ProdutoLimpeza
import br.com.local.toxihelp.domain.Medicamento
import androidx.recyclerview.widget.RecyclerView

class Categorias : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categorias)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val categorias = listOf<ICategoria>(
            Agrotoxico("Agrotoxico", "Nao coma", "#E1A8F0", listOf<IItem>()),
            AnimalPeconhento("Animal Peconhento", "De preferencia nao seja mordido", "#FFDE59", listOf<IItem>()),
            Cosmetico("Cosmetico", "Use, nao coma", "#FF66C4", listOf<IItem>()),
            PlantaToxica("Planta Toxica", "As vezes", "#FF5757", listOf<IItem>()),
            ProdutoLimpeza("Produto Limpeza", "Usa luva vei", "#C1FF72", listOf<IItem>()),
            Medicamento("Medicamento", "Tome remedios", "#D9D9D9", listOf<IItem>())
        )

        val rv = findViewById<RecyclerView>(R.id.rv_categorias)

        rv.layoutManager = LinearLayoutManager(this)

        rv.adapter = CategoriaAdapter(
            categorias = categorias
        ) { categoria ->
            Toast.makeText(this, "Funcionou", Toast.LENGTH_SHORT).show()
        }
    }
}