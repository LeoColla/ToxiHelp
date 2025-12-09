package br.com.local.toxihelp.domain

// interface para abstracao dos itens que compoem uma categoria
interface IItem {
    val nomePopular: String
    val sintIntox: String
    val primSocorro: String
}