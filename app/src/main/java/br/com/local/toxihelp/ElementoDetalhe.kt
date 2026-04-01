package br.com.local.toxihelp

import android.os.Bundle
import android.util.Log

import android.view.Gravity // Importação necessária para centralizar
import android.view.View

import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import android.graphics.Typeface
import java.util.Locale

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import br.com.local.toxihelp.data.mapper.ImagemMapper
import br.com.local.toxihelp.domain.AnimalPeconhento
import br.com.local.toxihelp.domain.Elemento
import br.com.local.toxihelp.domain.PlantaToxica
import br.com.local.toxihelp.domain.Medicamento
import br.com.local.toxihelp.domain.Agrotoxico
import br.com.local.toxihelp.domain.Cosmetico
import br.com.local.toxihelp.domain.ProdutoLimpeza
import androidx.core.view.isGone
import com.google.android.material.button.MaterialButton



class ElementoDetalhe : AppCompatActivity() {
    private val reposit by lazy {
        (application as ToxiHelpApplication).repository
    }
    private lateinit var container : LinearLayout

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
        // 1. O TÍTULO GRANDE E CENTRALIZADO (Identificador Único)


        // Adiciona a imagem principal acima do titulo
        if (!elemento.imagemPrincipal.isNullOrBlank()) {
            //caminho1 caminho 2 visivel e afins, eu defino la embaixo, por isso o margin top precisei definir tambem
            val imgTopo = criarImagemContainer(elemento.imagemPrincipal, null, true, 40)
            imgTopo?.let { container.addView(it) }
        }

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
        // Espaçador fixo de 40dp antes de começar a lista de botões
        container.addView(View(this).apply {
            layoutParams = LinearLayout.LayoutParams(1, 40.dp)
        })

        // 2. TÓPICOS EXPANSÍVEIS (Os Botões, que agora se auto-centralizam)
        // Detalhes específicos de cada tipo
        when (elemento) {
            is Medicamento -> adicionarCampoExpansivel("Função", elemento.funcao, )

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
        adicionarCampoExpansivel("Sintomas de Intoxicação", elemento.sintIntox, elemento.imagemSintIntox1, elemento.imagemSintIntox2)
        adicionarCampoExpansivel("Primeiros Socorros", elemento.primSocorro, elemento.imagemPrimSocorro1, elemento.imagemPrimSocorro2)

        Log.d("ElementoDetalheActivity", "Finalizado PopularUI")
    }

    private fun criarImagemContainer(caminho1: String?, caminho2: String? = null, visivel: Boolean = false,marginTop: Int = 15) : LinearLayout?{
        if (caminho1.isNullOrBlank() and caminho2.isNullOrBlank()) return null

        val linhaLayout = LinearLayout(this).apply {
            visibility = View.GONE
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = marginTop.dp
            }
        }

        if (visivel){
            linhaLayout.visibility = View.VISIBLE
        }

        val peso = if (!caminho1.isNullOrBlank() && !caminho2.isNullOrBlank()) 0.5f else 1f

        fun configurarImageView(): ImageView{
           val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    240,
                    peso
                ).apply {
                    topMargin = 15
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

        Log.d("ElementoDetalheActivity", "Imagens adicionadas")
        return linhaLayout
        //container.addView(linhaLayout)
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
    private fun adicionarCampoExpansivel(label: String, texto: String?, caminho1: String? = null, caminho2: String? = null) {
        if (texto.isNullOrBlank()) return

        // O BOTÃO (Cabeçalho) - Voltando para AppCompatButton
        val botao = androidx.appcompat.widget.AppCompatButton(this).apply {
            text = label
            textSize = 20f
            setTextColor(ContextCompat.getColor(context, android.R.color.white))

            // Aplica o seu XML que contém o shape arredondado
            background = ContextCompat.getDrawable(context, R.drawable.botao_arredondado)

            // Garante a cor vermelha (ou a cor do seu tint)
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.botao_vermelho_tint)

            // Aplica a sombra (elevation)
            elevation = 15.dp.toFloat()

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 20.dp // Usando .dp para consistência
                gravity = Gravity.CENTER_HORIZONTAL
                // Padding interno para o texto não encostar nas bordas do botão
                setPadding(60.dp, 0, 60.dp, 0)
            }
        }

        // O TEXTO (Conteúdo que começa escondido)
        val textoDescricao = TextView(this).apply {
            text = texto
            visibility = View.GONE
            textSize = 16f
            setPadding(15.dp, 15.dp, 15.dp, 15.dp)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setLineSpacing(0f, 1.2f)
            gravity = Gravity.FILL_HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val imageContainer = criarImagemContainer(caminho1, caminho2)

        // Lógica de Expandir/Recolher
        botao.setOnClickListener {
            val novoEstado = if (textoDescricao.isGone) View.VISIBLE else View.GONE

            textoDescricao.visibility = novoEstado
            imageContainer?.visibility = novoEstado
        }

        container.addView(botao)
        container.addView(textoDescricao)

        if (imageContainer != null) {
            container.addView(imageContainer)
        }
    }
    private val Int.dp: Int
        get() = (this * android.content.res.Resources.getSystem().displayMetrics.density).toInt()

    // NOVA EXTENSÃO PARA FLOAT (Para sombras e espessuras)
    private val Float.dp: Float
        get() = this * android.content.res.Resources.getSystem().displayMetrics.density
}

