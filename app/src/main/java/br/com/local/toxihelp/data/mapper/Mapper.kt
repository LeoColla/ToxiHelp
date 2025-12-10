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
