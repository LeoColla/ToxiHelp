package br.com.local.toxihelp.data.repository

import br.com.local.toxihelp.data.local.dao.*
import br.com.local.toxihelp.data.mapper.*
import br.com.local.toxihelp.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(
    private val categoriaDAO: CategoriaDAO,
) {
    fun getCategorias(): Flow<List<Categoria>> =
        categoriaDAO.getCategorias().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun insertCategorias(item: Categoria) =
        categoriaDAO.insertCategorias(item.toEntity())
}