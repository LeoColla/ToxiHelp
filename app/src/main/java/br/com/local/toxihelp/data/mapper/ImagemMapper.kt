package br.com.local.toxihelp.data.mapper

import br.com.local.toxihelp.R

object ImagemMapper {
    private val imagemMap = mapOf(
        "im_hospital" to R.drawable.im_hospital,
        "im_paracetamol" to R.drawable.im_paracetamol,
        "im_vomito" to R.drawable.im_vomito,
        "im_nao_provocar_vomito" to R.drawable.im_nao_provocar_vomito,
    )

    fun getImagemId(nome: String?): Int {
        return imagemMap[nome] ?: 0
    }
}