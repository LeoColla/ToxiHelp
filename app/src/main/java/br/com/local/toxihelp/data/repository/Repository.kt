package br.com.local.toxihelp.data.repository

import br.com.local.toxihelp.data.local.dao.*
import br.com.local.toxihelp.data.local.entity.ElementoResumo
import br.com.local.toxihelp.data.mapper.*
import br.com.local.toxihelp.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(
    private val categoriaDAO: CategoriaDAO,
    private val elementoDAO: ElementoDAO
) {
    fun getCategorias(): Flow<List<Categoria>> =
        categoriaDAO.getCategorias().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getCategoriaPorNome(nome: String): Categoria? {
        val entity = categoriaDAO.getCategoriaPorNome(nome)
        return entity?.toDomain()
    }

    suspend fun insertCategorias(item: Categoria) =
        categoriaDAO.insertCategorias(item.toEntity())

    fun getElementosResumoPorCategoria(nomeCategoria: String) : Flow<List<ElementoResumo>> =
        elementoDAO.getElementosResumoPorCategoria(nomeCategoria)


    suspend fun getElementoPorNomePopular(nomePopular: String): Elemento? {
        val entity = elementoDAO.getElementoPorNomePopular(nomePopular)
        return entity?.toDomain()
    }
}