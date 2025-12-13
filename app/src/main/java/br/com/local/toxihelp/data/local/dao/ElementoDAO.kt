package br.com.local.toxihelp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.local.toxihelp.data.local.entity.ElementoResumo
import br.com.local.toxihelp.data.local.entity.ElementoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ElementoDAO {
    @Query("SELECT nomePopular FROM elementos WHERE categoriaNome = :nomeCategoria")
    fun getElementosResumoPorCategoria(nomeCategoria: String): Flow<List<ElementoResumo>>

    // @Query("SELECT * FROM entidades_toxicas WHERE categoriaNome = :nomeCategoria")
    // fun getEntidadesCompletasPorCategoria(nomeCategoria: String): Flow<List<ElementoEntity>>

    @Query("SELECT * FROM elementos WHERE nomePopular = :nomePopular")
    suspend fun getElementoPorNomePopular(nomePopular: String): ElementoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entidades: List<ElementoEntity>)
}