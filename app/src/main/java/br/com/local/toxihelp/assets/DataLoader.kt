package br.com.local.toxihelp.assets

import android.content.res.AssetManager
import br.com.local.toxihelp.data.local.dao.CategoriaDAO
import br.com.local.toxihelp.data.local.entity.CategoriaEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import br.com.local.toxihelp.data.local.dao.ElementoDAO
import br.com.local.toxihelp.data.local.entity.ElementoEntity

class DataLoader(
    private val assets: AssetManager,
    private val categoriaDAO: CategoriaDAO,
    private val elementoDAO: ElementoDAO
) {
    // estamos acessando DAO diretamente
    private val gson = Gson()

    suspend fun populate() {
        loadCategorias()
        loadElementos()
    }

    suspend private fun loadCategorias() {
        try {
            Log.d("DataLoader", "loadCategorias: Iniciando carregamento de Categorias.")

            val json = read("categorias.json")
            Log.d("DataLoader", "loadCategorias: JSON carregado.")

            val listType = object : TypeToken<List<CategoriaEntity>>() {}.type
            val items: List<CategoriaEntity> = gson.fromJson(json, listType)

            Log.d("DataLoader", "loadCategorias: Inserindo Categorias no banco de dados.")
            items.forEach { categoriaDAO.insertCategorias(it) }
            Log.d("DataLoader", "loadCategorias: Carregamento concluído. Quantidade de categorias: ${items.size}")
        }catch (e: Exception){
            Log.e("DataLoader", "Erro ao carregar categorias: ${e.message}")
        }
    }

    suspend private fun loadElementos(){
        val arquivosJson = listOf(
            "agrotoxicos.json",
            "medicamentos.json",
            "plantasToxicas.json"
        )

        val listType = object : TypeToken<List<ElementoEntity>>() {}.type

        arquivosJson.forEach { arquivo ->
            try {
                Log.d("DataLoader", "loadElementos: Iniciando carregamento de '$arquivo'.")

                val json = read(arquivo)
                Log.d("DataLoader", "loadElementos: JSON carregado.")

                val items: List<ElementoEntity> = gson.fromJson(json, listType)

                Log.d("DataLoader", "loadElementos: Inserindo Elementos no banco de dados.")
                elementoDAO.insertAll(items)
                Log.d("DataLoader", "loadElementos: Carregamento concluído. Quantidade de Elementos: ${items.size}")
            }catch (e: Exception){
                Log.e("DataLoader", "Erro ao carregar Elementos de '$arquivo': ${e.message}")
            }
        }
    }

    private fun read(filename: String): String {
        return assets.open(filename).bufferedReader().use { it.readText() }
    }
}