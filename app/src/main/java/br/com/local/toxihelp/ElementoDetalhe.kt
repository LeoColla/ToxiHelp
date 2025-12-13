package br.com.local.toxihelp

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.com.local.toxihelp.domain.AnimalPeconhento
import br.com.local.toxihelp.domain.Elemento
import br.com.local.toxihelp.domain.PlantaToxica
import br.com.local.toxihelp.domain.Medicamento
import br.com.local.toxihelp.domain.Agrotoxico
import br.com.local.toxihelp.domain.Cosmetico
import br.com.local.toxihelp.domain.ProdutoLimpeza
import kotlinx.coroutines.launch

class ElementoDetalhe : AppCompatActivity() {
    private val reposit by lazy {
        (application as ToxiHelpApplication).repository
    }
    private lateinit var container : LinearLayout

    // valor entre 0 e 1
    private val propTexto = 0.7f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_elemento_detalhe)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nomePopular = intent.getStringExtra("NOME_POPULAR")

        // validacao de nulo
        if (nomePopular == null) {
            Log.e("ElementoDetalheActivity", "Nome popular não foi passado no Intent.")
            Toast.makeText(this, "Erro: Elemento não especificada.", Toast.LENGTH_LONG).show()
            finish()
            return // Para a execução do onCreate aqui
        }

        container = this.findViewById(R.id.element_container)
        container.removeAllViews()

        lifecycleScope.launch {
            val elemento = reposit.getElementoPorNomePopular(nomePopular)

            if (elemento != null){
                popularUI(elemento)
            }
        }
    }

    private fun popularUI(elemento: Elemento){
        Log.d("ElementoDetalheActivity", "Iniciando PopularUI")
        // cria um dicionario com a chave e valor
        // é a label / texto
        val camposEspecificos = when(elemento){
            is Medicamento -> mapOf(
                "Príncipio Ativo" to  elemento.nomePopular,
                "Nome Popular / Função" to elemento.funcao
            )
            is PlantaToxica -> mapOf(
                "Planta" to  elemento.nomeCientifico,
                "Nome Popular" to elemento.nomePopular,
                "Parte Tóxica" to elemento.parteToxica
            )
            is AnimalPeconhento -> mapOf(
                "Animal" to  elemento.nomeCientifico,
                "Nome Popular" to elemento.nomePopular,
                "Substância Tóxica - Características" to elemento.substanciaToxica
            )
            is Agrotoxico -> mapOf(
                "Substância" to  elemento.nomePopular,
                "Nome Popular / Função" to elemento.funcao
            )
            is Cosmetico -> mapOf(
                "Substância" to  elemento.nomePopular,
                "Produtos que contem a substância" to elemento.produto
            )
            is ProdutoLimpeza -> mapOf(
                "Substância" to  elemento.nomePopular,
                "Produtos que contem a substância" to elemento.produto,
                "Nome Popular" to elemento.nomePopular
            )
        }

        camposEspecificos.entries.forEachIndexed { index, entry ->
            val label = entry.key
            val texto = entry.value
            val isItalic = (index == 0) // apenas o primeiro item é italico

            adicionarCampo(label, texto, isItalic)
        }

        adicionarCampo("Sintomas de Intoxicação", elemento.sintIntox)
        adicionarCampo("Primeiros Socorros", elemento.primSocorro)
        Log.d("ElementoDetalheActivity", "Finalizado PopularUI")
    }

    private fun adicionarCampo(label: String, texto: String?, isItalic: Boolean = false){
        Log.d("ElementoDetalheActivity", "Adicionando campos: $label")
        // vamos criar a linha para cada "linha" da tabela
        // basicamente, temos um container (a linha em si)
        // e dentro dela duas caixas de texto (label e texto)

        if (texto.isNullOrBlank()) {
            return
            }

        val linhaLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16
            }
        }

        val labelTextView = TextView(this).apply {
            text = label
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 - propTexto
            )
            setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
        }

        val textoTextView = TextView(this).apply {
            text = texto
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                propTexto
            )
            if (isItalic) {
                setTypeface(null, android.graphics.Typeface.BOLD_ITALIC)
            }
        }

        linhaLayout.addView(labelTextView)
        linhaLayout.addView(textoTextView)

        container.addView(linhaLayout)
        Log.d("ElementoDetalheActivity", "$label adicionado")
    }
}