package br.com.local.toxihelp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.local.toxihelp.data.local.entity.EntidadeResumo
import br.com.local.toxihelp.data.local.entity.EntidadeToxicaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntidadeToxicaDAO {
    @Query("SELECT nomePopular FROM entidades_toxicas WHERE categoriaNome = :nomeCategoria")
    fun getEntidadesResumoPorCategoria(nomeCategoria: String): Flow<List<EntidadeResumo>>

    // @Query("SELECT * FROM entidades_toxicas WHERE categoriaNome = :nomeCategoria")
    // fun getEntidadesCompletasPorCategoria(nomeCategoria: String): Flow<List<EntidadeToxicaEntity>>

    @Query("SELECT * FROM entidades_toxicas WHERE nomePopular = :nomePopular")
    fun getEntidadePorNomePopular(nomePopular: String): EntidadeToxicaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entidades: List<EntidadeToxicaEntity>)
}