package br.com.local.toxihelp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "elementos")
data class ElementoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // nome da categoria para filtro
    val categoriaNome: String,

    // Campos comuns da interface IItem
    val nomePopular: String,
    val sintIntox: String,
    val primSocorro: String,

    // Campos específicos (podem ser nulos)
    val nomeCientifico: String?,
    val funcao: String?,           // Apenas para Medicamentos e Agrotoxico
    val parteToxica: String?,      // Apenas para Planta
    val substanciaToxica: String?, // Apenas para Animal
    val produto: String?,          // Apenas para Cosmético e Limpeza
    val substancia: String?        // Apenas para Limpeza
)