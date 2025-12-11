package br.com.local.toxihelp.data.mapper

import br.com.local.toxihelp.data.local.entity.*
import br.com.local.toxihelp.domain.*

// converte os dados do banco (entidade) para as classes (domain)

// categorias
fun CategoriaEntity.toDomain() = Categoria(
    nome = nome,
    intro = nome,
    colorCode = colorCode
)

fun Categoria.toEntity() = CategoriaEntity(
    id = 0,
    nome = nome,
    intro = nome,
    colorCode = colorCode
)

fun EntidadeToxicaEntity.toDomain(): EntidadeToxica {
    // Usamos o 'categoriaNome' para decidir qual tipo de objeto de domínio criar.
    // Isso é mais robusto do que verificar campos nulos.
    return when (categoriaNome) {
        "Agrotoxico" -> SubstanciaAgro(
            nomeCientifico = nomeCientifico ?: "",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Planta Toxica" -> Planta(
            nomeCientifico = nomeCientifico ?: "",
            parteToxica = parteToxica ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Animal Peconhento" -> Animal(
            nomeCientifico = nomeCientifico ?: "",
            substanciaToxica = substanciaToxica ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Cosmetico" -> SubstanciaCosm(
            produto = produto ?: "Não especificado",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Produto Limpeza" -> SubstanciaLimp(
            substancia = substancia ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Medicamento" -> PrincipioAtivo(
            funcao = funcao ?: "",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        else -> throw IllegalArgumentException("Tipo de entidade desconhecido: $categoriaNome")
    }
}

fun EntidadeToxica.toEntity(): EntidadeToxicaEntity {
    // Extrai os campos comuns da interface
    val nomePopular = this.nomePopular
    val sintIntox = this.sintIntox
    val primSocorro = this.primSocorro

    // Determina o nome da categoria e os campos específicos
    return when (this) {
        is Animal -> EntidadeToxicaEntity(
            categoriaNome = "Animal Peconhento",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            nomeCientifico = this.nomeCientifico,
            substanciaToxica = this.substanciaToxica,
            parteToxica = null, produto = null, substancia = null, funcao = null
        )
        is Planta -> EntidadeToxicaEntity(
            categoriaNome = "Planta Toxica",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            nomeCientifico = this.nomeCientifico,
            parteToxica = this.parteToxica,
            substanciaToxica = null, produto = null, substancia = null, funcao = null
        )
        is PrincipioAtivo -> EntidadeToxicaEntity(
            categoriaNome = "Medicamento",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            funcao = this.funcao,
            parteToxica = null, substanciaToxica = null, produto = null, substancia = null, nomeCientifico = null
        )
        is SubstanciaAgro -> EntidadeToxicaEntity(
            categoriaNome = "Agrotoxico",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            nomeCientifico = this.nomeCientifico,
            parteToxica = null, substanciaToxica = null, produto = null, substancia = null, funcao = null
        )
        is SubstanciaCosm -> EntidadeToxicaEntity(
            categoriaNome = "Cosmetico",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            produto = this.produto,
            nomeCientifico = null, parteToxica = null, substanciaToxica = null, substancia = null, funcao = null
        )
        is SubstanciaLimp -> EntidadeToxicaEntity(
            categoriaNome = "Produto Limpeza",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            substancia = this.substancia,
            nomeCientifico = null, parteToxica = null, substanciaToxica = null, produto = null, funcao = null
        )
    }
}