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
import android.widget.ScrollView

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

import androidx.core.text.HtmlCompat

class ElementoDetalhe : AppCompatActivity() {
    private val reposit by lazy {
        (application as ToxiHelpApplication).repository
    }
    private lateinit var container : LinearLayout
    private lateinit var scrollView : ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_elemento_detalhe)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar_detalhe)
        setSupportActionBar(toolbar)

        // Ativa o botão de "Home" (que por padrão é o voltar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Simula o voltar do sistema
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val nomePopular = intent.getStringExtra("NOME_POPULAR")

        // validacao de nulo
        if (nomePopular == null) {
            Log.e("ElementoDetalheActivity", "Nome popular não foi passado no Intent.")
            Toast.makeText(this, "Erro: Elemento não especificada.", Toast.LENGTH_LONG).show()
            finish()
            return // Para a execução do onCreate aqui
        }

        // Usado para rodar a tela ao clicar no botao
        scrollView = this.findViewById(R.id.scroll_detalhe)

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

        if (elemento is PlantaToxica){
            val imagemContainer = criarImagemContainer(elemento.imagemPrincipal, null,true)

            if (imagemContainer != null){
                container.addView(imagemContainer)
            }
        }else{
            val imagemContainer = criarImagemContainer(elemento.imagemPrincipal, elemento.imagemSecundaria,true)

            if (imagemContainer != null){
                container.addView(imagemContainer)
            }
        }

        // Usamos uma nova função para criar o título grande
        when (elemento) {
            is Medicamento -> {
                val nome = elemento.nomePopular ?: ""
                if (nome.contains( " - ")){
                    val partes = nome.split(" - ")
                    adicionarTituloGrande(partes[1])
                    adicionarSubtituloCentrado(texto = partes[0])
                }else {
                    adicionarTituloGrande(elemento.nomePopular)
                }
            }

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
            is Agrotoxico -> {
                val nome = elemento.nomePopular ?: ""
                if (nome.contains( " - ")){
                    val partes = nome.split(" - ")
                    adicionarTituloGrande(partes[1])
                    adicionarSubtituloCentrado(texto = partes[0])
                }else {
                    adicionarTituloGrande(elemento.nomePopular)
                }
            }
            is Cosmetico -> {
                val nome = elemento.nomePopular ?: ""
                if (nome.contains( " - ")){
                    val partes = nome.split(" - ")
                    adicionarTituloGrande(partes[1])
                    adicionarSubtituloCentrado(texto = partes[0])
                }else {
                    adicionarTituloGrande(elemento.nomePopular)
                }
            }
            is ProdutoLimpeza -> {
                adicionarTituloGrande(elemento.nomePopular)
                adicionarSubtituloCentrado("Substância", elemento.substancia)
            }
        }

        // Espaçamento maior entre o título e os botões
        val spacer = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(1, 40.dp)
        }
        container.addView(spacer)

        // 2. TÓPICOS EXPANSÍVEIS (Os Botões, que agora se auto-centralizam)
        // Detalhes específicos de cada tipo
        when (elemento) {
            is Medicamento -> adicionarCampoExpansivel("Função", elemento.funcao, )

            is PlantaToxica -> {
                adicionarCampoExpansivel("Parte Tóxica", elemento.parteToxica, elemento.imagemSecundaria, null)
                adicionarCampoExpansivel("Características", elemento.caracteristica)
                adicionarCampoExpansivel("Resumo", elemento.resumo)
            }

            is AnimalPeconhento -> {
                adicionarCampoExpansivel("Substâncias Tóxicas", elemento.substanciaToxica)
                adicionarCampoExpansivel("Características", elemento.caracteristica)
                adicionarCampoExpansivel("Resumo", elemento.resumo)
            }

            is Cosmetico -> adicionarCampoExpansivel("Produtos que Contêm a Substância", elemento.produto)

            is ProdutoLimpeza -> adicionarCampoExpansivel("Produtos que Contêm a Substância", elemento.produto)

            is Agrotoxico -> adicionarCampoExpansivel("Função", elemento.funcao)
        }

        // Campos Universais (Todo elemento tem esses)
        adicionarCampoExpansivel("Sintomas de Intoxicação", elemento.sintIntox, elemento.imagemSintIntox1, elemento.imagemSintIntox2)
        adicionarCampoExpansivel("Primeiros Socorros", elemento.primSocorro, elemento.imagemPrimSocorro1, elemento.imagemPrimSocorro2)

        Log.d("ElementoDetalheActivity", "Finalizado PopularUI")
    }

    private fun criarImagemContainer(caminho1: String?, caminho2: String? = null, visivel: Boolean = false, marginTop: Int = 15) : LinearLayout?{
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
                    400,
                    peso
                ).apply {
                    topMargin = 15
                    marginEnd = 8
                }
               scaleType = ImageView.ScaleType.FIT_CENTER
               minimumHeight = 300
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
            text = formataTexto(texto)
            textSize = 32f // Tamanho bem grande

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
    private fun adicionarSubtituloCentrado(label: String? = null, texto: String?) {
        if (texto.isNullOrBlank()) return

        val subtituloTv = TextView(this).apply {
            if (label != null){
                text = "$label: $texto"
            }else{
                text = "$texto"
            }

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

        // O BOTÃO (Cabeçalho)
        val botao = MaterialButton(this).apply {
            text = label.uppercase()
            textSize = 14f
            setTextColor(ContextCompat.getColor(context, R.color.botao_branco_tint))
            //background = ContextCompat.getDrawable(context, R.drawable.botao_arredondado)
            // Se você usar a cor vermelha da Main:
            backgroundTintList = ContextCompat.getColorStateList(context, R.color.botao_vermelho_tint)

            stateListAnimator = null
            elevation = 16.dp.toFloat()
            cornerRadius = 8.dp

            // NOVO: layout_gravity para centralizar o botão na horizontal
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Botão não estica mais
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(20.dp, 10.dp, 20.dp, 10.dp) // Usando .dp para consistência
                gravity = Gravity.CENTER_HORIZONTAL
                // Padding interno para o texto não encostar nas bordas do botão
                setPadding(0.dp, 16, 0.dp, 16)
            }
        }

        // O TEXTO (Conteúdo que começa escondido)
        val textoDescricao = TextView(this).apply {
            text = formataTexto(texto)
            visibility = View.GONE
            textSize = 16f
            setPadding(15.dp, 15.dp, 15.dp, 15.dp)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setLineSpacing(0f, 1.2f) // Corrige o erro anterior
            //gravity = Gravity.FILL_HORIZONTAL // Opcional: centraliza o texto da explicação

            // LINHAS NOVAS: Ativa o alinhamento justificado se o celular for Android 8.0 ou superior
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                justificationMode = android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // O texto pode usar a largura toda
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val imageContainer = criarImagemContainer(caminho1, caminho2)

        // Lógica de Expandir/Recolher
        botao.setOnClickListener {
            val naoVisivel = textoDescricao.isGone
            val novoEstado = if (naoVisivel) View.VISIBLE else View.GONE

            textoDescricao.visibility = novoEstado
            imageContainer?.visibility = novoEstado

            if (naoVisivel){
                // Pequeno atraso para rolar
                // Permite que a tela expanda o conteudo antes de rolar
                container.postDelayed({
                    val lugarVertical = botao.top

                    val scrollAnimator = android.animation.ObjectAnimator.ofInt(
                        scrollView,
                        "scrollY",
                        lugarVertical
                    )

                    scrollAnimator.duration = 500
                    scrollAnimator.interpolator = android.view.animation.AccelerateDecelerateInterpolator()
                    scrollAnimator.start()
                }, 200)
            }
        }

        container.addView(botao)
        container.addView(textoDescricao)

        if (imageContainer != null){
            container.addView(imageContainer)
        }

    }

    private fun formataTexto(texto : String) : CharSequence{
        val textoProcessado = texto.replace("\n", "<br>")

        val textoFormatado = HtmlCompat.fromHtml(
            textoProcessado,
            HtmlCompat.FROM_HTML_MODE_LEGACY)

        return textoFormatado
    }

    private val Int.dp: Int
        get() = (this * android.content.res.Resources.getSystem().displayMetrics.density).toInt()

    // NOVA EXTENSÃO PARA FLOAT (Para sombras e espessuras)
    private val Float.dp: Float
        get() = this * android.content.res.Resources.getSystem().displayMetrics.density
}

