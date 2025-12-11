package br.com.local.toxihelp.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.local.toxihelp.assets.DataLoader
import br.com.local.toxihelp.data.local.dao.*
import br.com.local.toxihelp.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CategoriaEntity::class,
        EntidadeToxicaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriaDAO(): CategoriaDAO
    abstract fun entidadeToxicaDAO(): EntidadeToxicaDAO

    // companion eh semelhante a classe estatica
    // vamos ter apenas um para todas as instancias
    companion object {

        // Volatile torna a variavel atualizada visivel para todas as threads
        // evita problemas de corrida
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "toxiHelp.db"
                )
                    .addCallback(DatabaseCallback(context))
                    .build()

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
            val catDAO = database.categoriaDAO()
            val entDAO = database.entidadeToxicaDAO()

            CoroutineScope(Dispatchers.IO).launch {
                Log.d("AppDatabase", "Coroutine de população iniciada.")
                DataLoader(context.assets, catDAO, entDAO).populate()
                Log.d("AppDatabase", "Coroutine de população concluída.")
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d("AppDatabase", "DatabaseCallback.onOpen: O banco de dados existente foi aberto.")
        }
    }

}