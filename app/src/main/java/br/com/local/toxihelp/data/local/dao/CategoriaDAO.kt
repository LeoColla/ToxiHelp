package br.com.local.toxihelp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.local.toxihelp.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDAO {
    // flow e usado para coletar dados assincronos
    // assim atualizamos a lista mais de uma vez
    @Query("SELECT * FROM categorias")
    fun getCategorias(): Flow<List<CategoriaEntity>>

    @Insert
    suspend fun insertCategorias(e: CategoriaEntity)

    @Delete
    suspend fun deleteCategorias(e: CategoriaEntity)
}