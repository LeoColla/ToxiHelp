package br.com.local.toxihelp.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.local.toxihelp.assets.DataLoader
import br.com.local.toxihelp.data.local.dao.*
import br.com.local.toxihelp.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO modificar AndroidManifest para android:allowBackup="true"
// Apos isso, toda mudança no banco deve ser feita por meio de Migração
@Database(
    entities = [
        CategoriaEntity::class,
        ElementoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriaDAO(): CategoriaDAO
    abstract fun elementoDAO(): ElementoDAO

    // companion eh semelhante a classe estatica
    // vamos ter apenas um para todas as instancias
    companion object {

        // Volatile torna a variavel atualizada visivel para todas as threads
        // evita problemas de corrida
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // ?: operador elvis
        // retorna instancia se houver valor
        // roda o syncronized caso contrario

        // syncronized eh um semaforo
        // ele permite que apenas uma thread acesse o bloco de codigo por vez
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "toxiHelp.db"
                )
                    .addCallback(DatabaseCallback(context))
                    .fallbackToDestructiveMigration(true)
                    .build()

                // TODO remover fallback apos adicionar migração

                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("AppDatabase", "DatabaseCallback.onCreate: O banco de dados está sendo criado.")


            // Acessamos a instância diretamente após a criação no companion object
            val database = INSTANCE!!
            val categoriaDAO = database.categoriaDAO()
            val elementoDAO = database.elementoDAO()

            CoroutineScope(Dispatchers.IO).launch {
                Log.d("AppDatabase", "Coroutine de população iniciada.")
                DataLoader(context.assets, categoriaDAO, elementoDAO).populate()
                Log.d("AppDatabase", "Coroutine de população concluída.")
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d("AppDatabase", "DatabaseCallback.onOpen: O banco de dados existente foi aberto.")
        }
    }

}