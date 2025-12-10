package br.com.local.toxihelp.assets

import android.content.res.AssetManager
import br.com.local.toxihelp.data.local.dao.CategoriaDAO
import br.com.local.toxihelp.data.local.entity.CategoriaEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log

class DataLoader(
    private val assets: AssetManager,
    private val dao: CategoriaDAO
) {
    // estamos acessando DAO diretamente
    private val gson = Gson()

    suspend fun populate() {
        loadCategorias()
    }

    suspend private fun loadCategorias() {
        Log.d("DataLoader", "loadCategorias: Iniciando carregamento de categorias.")
        val json = read("categorias.json")
        Log.d("DataLoader", "loadCategorias: JSON carregado.")

        val listType = object : TypeToken<List<CategoriaEntity>>() {}.type
        val items: List<CategoriaEntity> = gson.fromJson(json, listType)

        Log.d("DataLoader", "loadCategorias: Inserindo categorias no banco de dados.")
        items.forEach { dao.insertCategorias(it) }
        Log.d("DataLoader", "loadCategorias: Carregamento concluído.")
    }

    private fun read(filename: String): String {
        return assets.open(filename).bufferedReader().use { it.readText() }
    }
}