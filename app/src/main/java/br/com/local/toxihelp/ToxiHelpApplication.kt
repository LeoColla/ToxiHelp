package br.com.local.toxihelp

import android.app.Application
import br.com.local.toxihelp.data.local.AppDatabase
import br.com.local.toxihelp.data.repository.Repository

// Classe de Application customizada para servir como um contêiner de dependências.

class ToxiHelpApplication : Application() {
    // Usamos 'lazy' para que o banco de dados e o repositório sejam criados
    // somente quando forem acessados pela primeira vez.
    private val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { Repository(database.categoriaDAO()) }
}
