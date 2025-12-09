package br.com.local.toxihelp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val botaoIntroducao: Button = findViewById(R.id.b_introducao)
        botaoIntroducao.setOnClickListener{ GoIntroducao() }

        val botaoCredito: Button = findViewById(R.id.b_credito)
        botaoCredito.setOnClickListener{ GoCreditos() }

        val botaoCategorias: Button = findViewById(R.id.b_categoria)
        botaoCategorias.setOnClickListener{ GoCategorias() }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun GoIntroducao(){
        val intentIntroducao = Intent(this,Introducao::class.java)
        startActivity(intentIntroducao)
    }

    private fun GoCreditos(){
        val intentCreditos = Intent(this,Creditos::class.java)
        startActivity(intentCreditos)
    }

    private fun GoCategorias(){
        val intentCategorias = Intent(this,Categorias::class.java)
        startActivity(intentCategorias)
    }
}