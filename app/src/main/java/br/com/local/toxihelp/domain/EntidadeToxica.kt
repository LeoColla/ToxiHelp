package br.com.local.toxihelp.domain

// Representa uma entidade tóxica.
// sealed significa que todas as subclasses devem permanecer neste arquivo.

sealed class EntidadeToxica : IItem

// tipos concretos de Item


// para fins de simplificacao, vou usar o Nome cientifico do principio ativo
// como nome popular, e a funcao como descricao
data class PrincipioAtivo(
    val funcao: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : EntidadeToxica()

data class SubstanciaAgro(
    val nomeCientifico: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : EntidadeToxica()

data class Planta(
    val nomeCientifico: String,
    val parteToxica: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : EntidadeToxica()

data class Animal(
    val nomeCientifico: String,
    val substanciaToxica: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : EntidadeToxica()

data class SubstanciaCosm(
    val produto: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : EntidadeToxica()

data class SubstanciaLimp(
    val substancia: String,
    override val nomePopular: String,
    override val sintIntox: String,
    override val primSocorro: String
) : EntidadeToxica()