package br.com.local.toxihelp

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.view.Gravity // Importação necessária para centralizar
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.com.local.toxihelp.data.mapper.ImagemMapper
import br.com.local.toxihelp.domain.AnimalPeconhento
import br.com.local.toxihelp.domain.Elemento
import br.com.local.toxihelp.domain.PlantaToxica
import br.com.local.toxihelp.domain.Medicamento
import br.com.local.toxihelp.domain.Agrotoxico
import br.com.local.toxihelp.domain.Cosmetico
import br.com.local.toxihelp.domain.ProdutoLimpeza
import br.com.local.toxihelp.domain.*
import kotlinx.coroutines.launch
import java.util.* // Importação necessária para o uppercase

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

        // NOVO: Garante que o LinearLayout centralize todos os seus filhos horizontalmente
        container.gravity = Gravity.CENTER_HORIZONTAL

        lifecycleScope.launch {
            val elemento = reposit.getElementoPorNomePopular(nomePopular)

            if (elemento != null){
                popularUI(elemento)
            }
        }
    }

    private fun popularUI(elemento: Elemento) {
        Log.d("ElementoDetalheActivity", "Iniciando PopularUI")
        // cria um dicionario com a chave e valor
        // é a label / texto
        val camposEspecificos = when (elemento) {
            is Medicamento -> mapOf(
                "Príncipio Ativo" to elemento.nomePopular,
                "Nome Popular / Função" to elemento.funcao
            )

            is PlantaToxica -> mapOf(
                "Planta" to elemento.nomeCientifico,
                "Nome Popular" to elemento.nomePopular,
                "Parte Tóxica" to elemento.parteToxica,
                "Caracteristica" to elemento.caracteristica,
                "Resumo" to elemento.resumo
            )

            is AnimalPeconhento -> mapOf(
                "Animal" to elemento.nomeCientifico,
                "Nome Popular" to elemento.nomePopular,
                "Substância Tóxica - Características" to elemento.substanciaToxica
            )

            is Agrotoxico -> mapOf(
                "Substância" to elemento.nomePopular,
                "Nome Popular / Função" to elemento.funcao
            )

            is Cosmetico -> mapOf(
                "Substância" to elemento.nomePopular,
                "Produtos que contem a substância" to elemento.produto
            )

            is ProdutoLimpeza -> mapOf(
                "Substância" to elemento.substancia,
                "Produtos que contem a substância" to elemento.produto,
                "Nome Popular" to elemento.nomePopular
            )
        }

        camposEspecificos.entries.forEachIndexed { index, entry ->
            val label = entry.key
            val texto = entry.value
            val isItalic = (index == 0) // apenas o primeiro item é italico

            adicionarCampo(label, texto, isItalic)

            adicionarImagem(elemento.imagemPrincipal)

            adicionarCampo("Sintomas de Intoxicação", elemento.sintIntox)
            adicionarImagem(elemento.imagemSintIntox1, elemento.imagemSintIntox2)

            adicionarCampo("Primeiros Socorros", elemento.primSocorro)
            adicionarImagem(elemento.imagemPrimSocorro1, elemento.imagemPrimSocorro2)

            Log.d("ElementoDetalheActivity", "Finalizado PopularUI")
        }
    }

    private fun popularUI2(elemento: Elemento) {
        // 1. O TÍTULO GRANDE E CENTRALIZADO (Identificador Único)
        // Usamos uma nova função para criar o título grande
        when (elemento) {
            is Medicamento -> adicionarTituloGrande(elemento.nomePopular)
            is PlantaToxica -> {
                // Para planta, o identificador principal costuma ser o científico
                adicionarTituloGrande(elemento.nomeCientifico)
                // Adicionamos o nome popular como um subtítulo
                adicionarSubtituloCentrado("Nome Popular", elemento.nomePopular)
            }
            is AnimalPeconhento -> {
                adicionarTituloGrande(elemento.nomeCientifico)
                adicionarSubtituloCentrado("Nome Popular", elemento.nomePopular)
            }
            is Agrotoxico -> adicionarTituloGrande(elemento.nomePopular)
            is Cosmetico -> adicionarTituloGrande(elemento.nomePopular)
            is ProdutoLimpeza -> {
                adicionarTituloGrande(elemento.nomePopular)
                adicionarSubtituloCentrado("Substância", elemento.substancia)
            }
        }

        // Espaçamento maior entre o título e os botões
        val spacer = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(1, 60)
        }
        container.addView(spacer)

        // 2. TÓPICOS EXPANSÍVEIS (Os Botões, que agora se auto-centralizam)
        // Detalhes específicos de cada tipo
        when (elemento) {
            is Medicamento -> adicionarCampoExpansivel("Função", elemento.funcao)
            is PlantaToxica -> {
                adicionarCampoExpansivel("Parte Tóxica", elemento.parteToxica)
                adicionarCampoExpansivel("Características", elemento.caracteristica)
                adicionarCampoExpansivel("Resumo", elemento.resumo)
            }
            is AnimalPeconhento -> {
                adicionarCampoExpansivel("Toxina e Características", elemento.substanciaToxica)
            }
            is Cosmetico -> adicionarCampoExpansivel("Onde é encontrado", elemento.produto)
            is ProdutoLimpeza -> adicionarCampoExpansivel("Onde é encontrado", elemento.produto)
            is Agrotoxico -> adicionarCampoExpansivel("Função", elemento.funcao)
        }

        // Campos Universais (Todo elemento tem esses)
        adicionarCampoExpansivel("Sintomas de Intoxicação", elemento.sintIntox)
        adicionarCampoExpansivel("Primeiros Socorros", elemento.primSocorro)
    }

    private fun adicionarImagem(caminho1: String?, caminho2: String? = null) {
        if (caminho1.isNullOrBlank() and caminho2.isNullOrBlank()) return

        val linhaLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16
            }
        }
        val peso = if (!caminho1.isNullOrBlank() && !caminho2.isNullOrBlank()) 0.5f else 1f

        fun configurarImageView(): ImageView{
           val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    240,
                    peso
                ).apply {
                    topMargin = 16
                    marginEnd = 8
                }
                //scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
            return imageView
        }


        if (!caminho1.isNullOrBlank()) {
            val imagemId = ImagemMapper.getImagemId(caminho1)

            if (imagemId != 0){
                configurarImageView().let { imageView ->
                    linhaLayout.addView(imageView)

                    com.bumptech.glide.Glide.with(this)
                        .load(imagemId)
                        .into(imageView)
                }

                Log.d("ElementoDetalheActivity", "Imagem $caminho1 adicionada")
            }
        }

        if (!caminho2.isNullOrBlank()) {
            val imagemId = ImagemMapper.getImagemId(caminho2)

            if (imagemId != 0){
                configurarImageView().let { imageView ->
                    linhaLayout.addView(imageView)

                    com.bumptech.glide.Glide.with(this)
                        .load(imagemId)
                        .into(imageView)
                }

                Log.d("ElementoDetalheActivity", "Imagem $caminho2 adicionada")
            }
        }

        container.addView(linhaLayout)
        Log.d("ElementoDetalheActivity", "Imagens adicionadas")
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

    /**
     * NOVA FUNÇÃO: Cria o título principal, grande, bold, uppercase e centralizado.
     * Não tem label (ex: "Medicamento:"). É só o nome puro.
     */
    private fun adicionarTituloGrande(texto: String?) {
        if (texto.isNullOrBlank()) return

        val tituloTv = TextView(this).apply {
            text = texto.uppercase(Locale.getDefault()) // Força o MAIÚSCULO
            textSize = 32f // Tamanho bem grande
            setTypeface(null, Typeface.BOLD) // Negrito
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            gravity = Gravity.CENTER // Centraliza o texto dentro da view
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // A view se ajusta ao texto
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 20
                setPadding(30, 0, 30, 0)
            }
        }
        container.addView(tituloTv)
    }

    /**
     * NOVA FUNÇÃO: Cria um subtítulo centralizado para informações secundárias
     * (ex: Nome Popular de uma planta). Formato: **Nome Popular:** *Texto*
     */
    private fun adicionarSubtituloCentrado(label: String, texto: String?) {
        if (texto.isNullOrBlank()) return

        val subtituloTv = TextView(this).apply {
            text = "$label: $texto"
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            gravity = Gravity.CENTER
            setPadding(30, 0, 30, 0)
            setTypeface(null, Typeface.BOLD_ITALIC)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 10 }
        }
        container.addView(subtituloTv)
    }

    /**
     * Modificada para os botões se auto-centralizarem horizontalmente.
     */
    private fun adicionarCampoExpansivel(label: String, texto: String?) {
        if (texto.isNullOrBlank()) return

        // O BOTÃO (Cabeçalho)
        val botao = androidx.appcompat.widget.AppCompatButton(this).apply {
            text = label
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            background = ContextCompat.getDrawable(context, R.drawable.botao_arredondado)
            // Se você usar a cor vermelha da Main:
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.botao_vermelho_tint)
            elevation = 8f

            // NOVO: layout_gravity para centralizar o botão na horizontal
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Botão não estica mais
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 30
                gravity = Gravity.CENTER_HORIZONTAL // Centraliza o botão no pai
                // Adicionamos um padding interno lateral para o botão não ficar espremido
                setPadding(60, 0, 60, 0)
            }
        }
        //SO PRA COMITAR DNV
        // O TEXTO (Conteúdo que começa escondido)
        val textoDescricao = TextView(this).apply {
            text = texto
            visibility = View.GONE
            textSize = 16f
            setPadding(40, 20, 40, 20)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setLineSpacing(0f, 1.2f) // Corrige o erro anterior
            gravity = Gravity.CENTER // Opcional: centraliza o texto da explicação
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // O texto pode usar a largura toda
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Lógica de Expandir/Recolher
        botao.setOnClickListener {
            if (textoDescricao.visibility == View.GONE) {
                textoDescricao.visibility = View.VISIBLE
            } else {
                textoDescricao.visibility = View.GONE
            }
        }

        container.addView(botao)
        container.addView(textoDescricao)
    }
}

