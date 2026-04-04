package br.com.local.toxihelp.domain

// interface para abstracao dos itens que compoem uma categoria
interface IElemento {
    val nomePopular: String
    val sintIntox: String
    val primSocorro: String

    val imagemPrincipal: String?
    val imagemSecundaria: String?
    val imagemSintIntox1: String?
    val imagemSintIntox2: String?
    val imagemPrimSocorro1: String?
    val imagemPrimSocorro2: String?
}