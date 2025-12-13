package br.com.local.toxihelp.domain

// Representa uma entidade tóxica.
// sealed significa que todas as subclasses devem permanecer neste arquivo.

sealed class Elemento : IElemento

// tipos concretos de Item


// para fins de simplificacao, vou usar o Nome cientifico do principio ativo
// como nome popular, e a funcao como descricao
data class Medicamento(
    val funcao: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : Elemento()

data class Agrotoxico(
    val funcao: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : Elemento()

data class PlantaToxica(
    val nomeCientifico: String,
    val parteToxica: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : Elemento()

data class AnimalPeconhento(
    val nomeCientifico: String,
    val substanciaToxica: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : Elemento()

data class Cosmetico(
    val produto: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : Elemento()

data class ProdutoLimpeza(
    val substancia: String,
    val produto: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : Elemento()