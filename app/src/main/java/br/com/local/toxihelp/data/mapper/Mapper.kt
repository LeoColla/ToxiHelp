package br.com.local.toxihelp.data.mapper

import br.com.local.toxihelp.data.local.entity.*
import br.com.local.toxihelp.domain.*

// converte os dados do banco (entidade) para as classes (domain)

// categorias
fun CategoriaEntity.toDomain() = Categoria(
    nome = nome,
    intro = intro,
    colorCode = colorCode
)

fun Categoria.toEntity() = CategoriaEntity(
    id = 0,
    nome = nome,
    intro = intro,
    colorCode = colorCode
)

fun ElementoEntity.toDomain(): Elemento {
    // Usamos o 'categoriaNome' para decidir qual tipo de objeto de domínio criar.
    // Isso é mais robusto do que verificar campos nulos.
    return when (categoriaNome) {
        "Agrotoxico" -> Agrotoxico(
            funcao = funcao ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Planta Toxica" -> PlantaToxica(
            nomeCientifico = nomeCientifico ?: "Não especificado",
            parteToxica = parteToxica ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Animal Peconhento" -> AnimalPeconhento(
            nomeCientifico = nomeCientifico ?: "Não especificado",
            substanciaToxica = substanciaToxica ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Cosmetico" -> Cosmetico(
            produto = produto ?: "Não especificado",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        "Produto Limpeza" -> ProdutoLimpeza(
            substancia = substancia ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            produto = produto ?: "Não especificado"
        )
        "Medicamento" -> Medicamento(
            funcao = funcao ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro
        )
        else -> throw IllegalArgumentException("Tipo de elemento desconhecido: $categoriaNome")
    }
}

fun Elemento.toEntity(): ElementoEntity {
    // Extrai os campos comuns da interface
    val nomePopular = this.nomePopular
    val sintIntox = this.sintIntox
    val primSocorro = this.primSocorro

    // Determina o nome da categoria e os campos específicos
    return when (this) {
        is AnimalPeconhento -> ElementoEntity(
            categoriaNome = "Animal Peconhento",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            nomeCientifico = this.nomeCientifico,
            substanciaToxica = this.substanciaToxica,
            parteToxica = null, produto = null, substancia = null, funcao = null
        )
        is PlantaToxica -> ElementoEntity(
            categoriaNome = "Planta Toxica",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            nomeCientifico = this.nomeCientifico,
            parteToxica = this.parteToxica,
            substanciaToxica = null, produto = null, substancia = null, funcao = null
        )
        is Medicamento -> ElementoEntity(
            categoriaNome = "Medicamento",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            funcao = this.funcao,
            parteToxica = null, substanciaToxica = null, produto = null, substancia = null, nomeCientifico = null
        )
        is Agrotoxico -> ElementoEntity(
            categoriaNome = "Agrotoxico",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            funcao = this.funcao,
            parteToxica = null, substanciaToxica = null, produto = null, substancia = null, nomeCientifico = null
        )
        is Cosmetico -> ElementoEntity(
            categoriaNome = "Cosmetico",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            produto = this.produto,
            nomeCientifico = null, parteToxica = null, substanciaToxica = null, substancia = null, funcao = null
        )
        is ProdutoLimpeza -> ElementoEntity(
            categoriaNome = "Produto Limpeza",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            substancia = this.substancia,
            produto = this.produto,
            nomeCientifico = null, parteToxica = null, substanciaToxica = null, funcao = null
        )
    }
}