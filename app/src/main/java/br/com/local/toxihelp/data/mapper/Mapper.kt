package br.com.local.toxihelp.data.mapper

import br.com.local.toxihelp.data.local.entity.*
import br.com.local.toxihelp.domain.*

// converte os dados do banco (entidade) para as classes (domain)

// categorias
fun CategoriaEntity.toDomain() = Categoria(
    nome = nome,
    intro = intro
)

fun Categoria.toEntity() = CategoriaEntity(
    id = 0,
    nome = nome,
    intro = intro
)

fun ElementoEntity.toDomain(): Elemento {
    // Usamos o 'categoriaNome' para decidir qual tipo de objeto de domínio criar.
    // Isso é mais robusto do que verificar campos nulos.
    return when (categoriaNome) {
        "Agrotóxico" -> Agrotoxico(
            funcao = funcao ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            imagemPrincipal = imagemPrincipal,
            imagemSintIntox1 = imagemSintIntox1,
            imagemSintIntox2 = imagemSintIntox2,
            imagemPrimSocorro1 = imagemPrimSocorro1,
            imagemPrimSocorro2 = imagemPrimSocorro2
        )
        "Planta Tóxica" -> PlantaToxica(
            nomeCientifico = nomeCientifico ?: "Não especificado",
            parteToxica = parteToxica ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            caracteristica = caracteristica ?: "Não especificada",
            resumo = resumo ?: "Não especificada",
            imagemPrincipal = imagemPrincipal,
            imagemSintIntox1 = imagemSintIntox1,
            imagemSintIntox2 = imagemSintIntox2,
            imagemPrimSocorro1 = imagemPrimSocorro1,
            imagemPrimSocorro2 = imagemPrimSocorro2
        )
        "Animal Peçonhento" -> AnimalPeconhento(
            nomeCientifico = nomeCientifico ?: "Não especificado",
            substanciaToxica = substanciaToxica ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            imagemPrincipal = imagemPrincipal,
            imagemSintIntox1 = imagemSintIntox1,
            imagemSintIntox2 = imagemSintIntox2,
            imagemPrimSocorro1 = imagemPrimSocorro1,
            imagemPrimSocorro2 = imagemPrimSocorro2
        )
        "Cosmético" -> Cosmetico(
            produto = produto ?: "Não especificado",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            imagemPrincipal = imagemPrincipal,
            imagemSintIntox1 = imagemSintIntox1,
            imagemSintIntox2 = imagemSintIntox2,
            imagemPrimSocorro1 = imagemPrimSocorro1,
            imagemPrimSocorro2 = imagemPrimSocorro2
        )
        "Produto de Limpeza" -> ProdutoLimpeza(
            substancia = substancia ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            produto = produto ?: "Não especificado",
            imagemPrincipal = imagemPrincipal,
            imagemSintIntox1 = imagemSintIntox1,
            imagemSintIntox2 = imagemSintIntox2,
            imagemPrimSocorro1 = imagemPrimSocorro1,
            imagemPrimSocorro2 = imagemPrimSocorro2
        )
        "Medicamento" -> Medicamento(
            funcao = funcao ?: "Não especificada",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            imagemPrincipal = imagemPrincipal,
            imagemSintIntox1 = imagemSintIntox1,
            imagemSintIntox2 = imagemSintIntox2,
            imagemPrimSocorro1 = imagemPrimSocorro1,
            imagemPrimSocorro2 = imagemPrimSocorro2
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
            parteToxica = null, produto = null, substancia = null, funcao = null, caracteristica = null, resumo = null,
            imagemPrincipal = this.imagemPrincipal,
            imagemSintIntox1 = this.imagemSintIntox1,
            imagemSintIntox2 = this.imagemSintIntox2,
            imagemPrimSocorro1 = this.imagemPrimSocorro1,
            imagemPrimSocorro2 = this.imagemPrimSocorro2
        )
        is PlantaToxica -> ElementoEntity(
            categoriaNome = "Planta Toxica",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            nomeCientifico = this.nomeCientifico,
            parteToxica = this.parteToxica,
            caracteristica = this.caracteristica,
            resumo = this.resumo,
            substanciaToxica = null, produto = null, substancia = null, funcao = null,
            imagemPrincipal = this.imagemPrincipal,
            imagemSintIntox1 = this.imagemSintIntox1,
            imagemSintIntox2 = this.imagemSintIntox2,
            imagemPrimSocorro1 = this.imagemPrimSocorro1,
            imagemPrimSocorro2 = this.imagemPrimSocorro2
        )
        is Medicamento -> ElementoEntity(
            categoriaNome = "Medicamento",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            funcao = this.funcao,
            parteToxica = null, substanciaToxica = null, produto = null, substancia = null, nomeCientifico = null, caracteristica = null, resumo = null,
            imagemPrincipal = this.imagemPrincipal,
            imagemSintIntox1 = this.imagemSintIntox1,
            imagemSintIntox2 = this.imagemSintIntox2,
            imagemPrimSocorro1 = this.imagemPrimSocorro1,
            imagemPrimSocorro2 = this.imagemPrimSocorro2
        )
        is Agrotoxico -> ElementoEntity(
            categoriaNome = "Agrotoxico",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            funcao = this.funcao,
            parteToxica = null, substanciaToxica = null, produto = null, substancia = null, nomeCientifico = null, caracteristica = null, resumo = null,
            imagemPrincipal = this.imagemPrincipal,
            imagemSintIntox1 = this.imagemSintIntox1,
            imagemSintIntox2 = this.imagemSintIntox2,
            imagemPrimSocorro1 = this.imagemPrimSocorro1,
            imagemPrimSocorro2 = this.imagemPrimSocorro2
        )
        is Cosmetico -> ElementoEntity(
            categoriaNome = "Cosmetico",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            produto = this.produto,
            nomeCientifico = null, parteToxica = null, substanciaToxica = null, substancia = null, funcao = null, caracteristica = null, resumo = null,
            imagemPrincipal = this.imagemPrincipal,
            imagemSintIntox1 = this.imagemSintIntox1,
            imagemSintIntox2 = this.imagemSintIntox2,
            imagemPrimSocorro1 = this.imagemPrimSocorro1,
            imagemPrimSocorro2 = this.imagemPrimSocorro2
        )
        is ProdutoLimpeza -> ElementoEntity(
            categoriaNome = "Produto Limpeza",
            nomePopular = nomePopular,
            sintIntox = sintIntox,
            primSocorro = primSocorro,
            substancia = this.substancia,
            produto = this.produto,
            nomeCientifico = null, parteToxica = null, substanciaToxica = null, funcao = null, caracteristica = null, resumo = null,
            imagemPrincipal = this.imagemPrincipal,
            imagemSintIntox1 = this.imagemSintIntox1,
            imagemSintIntox2 = this.imagemSintIntox2,
            imagemPrimSocorro1 = this.imagemPrimSocorro1,
            imagemPrimSocorro2 = this.imagemPrimSocorro2
        )
    }
}