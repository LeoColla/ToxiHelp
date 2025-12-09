package br.com.local.toxihelp.domain

// abstracao para categorias
interface ICategoria {
    val nome: String
    val intro: String
    val colorCode: String
    val itens: List<IItem>
}

// categorias concretas

data class Medicamento(
    override val nome: String,
    override val intro: String,
    override val colorCode: String,
    override val itens: List<IItem>
) : ICategoria

data class Agrotoxico(
    override val nome: String,
    override val intro: String,
    override val colorCode: String,
    override val itens: List<IItem>
) : ICategoria

data class PlantaToxica(
    override val nome: String,
    override val intro: String,
    override val colorCode: String,
    override val itens: List<IItem>
) : ICategoria

data class AnimalPeconhento(
    override val nome: String,
    override val intro: String,
    override val colorCode: String,
    override val itens: List<IItem>
) : ICategoria

data class Cosmetico(
    override val nome: String,
    override val intro: String,
    override val colorCode: String,
    override val itens: List<IItem>
) : ICategoria

data class ProdutoLimpeza(
    override val nome: String,
    override val intro: String,
    override val colorCode: String,
    override val itens: List<IItem>
) : ICategoria