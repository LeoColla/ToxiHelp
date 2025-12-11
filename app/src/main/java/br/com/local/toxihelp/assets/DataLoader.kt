package br.com.local.toxihelp.assets

import android.content.res.AssetManager
import br.com.local.toxihelp.data.local.dao.CategoriaDAO
import br.com.local.toxihelp.data.local.entity.CategoriaEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import br.com.local.toxihelp.data.local.dao.EntidadeToxicaDAO
import br.com.local.toxihelp.data.local.entity.EntidadeToxicaEntity

class DataLoader(
    private val assets: AssetManager,
    private val catDAO: CategoriaDAO,
    private val entDAO: EntidadeToxicaDAO
) {
    // estamos acessando DAO diretamente
    private val gson = Gson()

    suspend fun populate() {
        loadCategorias()
        loadEntidades()
    }

    suspend private fun loadCategorias() {
        try {
            Log.d("DataLoader", "loadCategorias: Iniciando carregamento de Categorias.")
            val json = read("categorias.json")
            Log.d("DataLoader", "loadCategorias: JSON carregado.")

            val listType = object : TypeToken<List<CategoriaEntity>>() {}.type
            val items: List<CategoriaEntity> = gson.fromJson(json, listType)

            Log.d("DataLoader", "loadCategorias: Inserindo Categorias no banco de dados.")
            items.forEach { catDAO.insertCategorias(it) }
            Log.d("DataLoader", "loadCategorias: Carregamento concluído. Quantidade de categorias: ${items.size}")
        }catch (e: Exception){
            Log.e("DataLoader", "Erro ao carregar categorias: ${e.message}")
        }
    }

    suspend private fun loadEntidades(){
        try {
            Log.d("DataLoader", "loadEntidades: Iniciando carregamento de Entidades.")
            val json = read("entidades.json")
            Log.d("DataLoader", "loadEntidades: JSON carregado.")

            val listType = object : TypeToken<List<EntidadeToxicaEntity>>() {}.type
            val items: List<EntidadeToxicaEntity> = gson.fromJson(json, listType)

            Log.d("DataLoader", "loadEntidades: Inserindo Entidades no banco de dados.")
            entDAO.insertAll(items)
            Log.d("DataLoader", "loadEntidades: Carregamento concluído. Quantidade de entidades: ${items.size}")
        }catch (e: Exception){
            Log.e("DataLoader", "Erro ao carregar entidades: ${e.message}")
        }

    }

    private fun read(filename: String): String {
        return assets.open(filename).bufferedReader().use { it.readText() }
    }
}