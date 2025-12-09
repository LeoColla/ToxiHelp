package br.com.local.toxihelp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Button // Import necessário para referenciar o Button
import android.content.Intent // Import necessário para usar Intent
import br.com.local.toxihelp.Introducao // Import necessário para sua Activity de destino


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val botaoIntroducao: Button = findViewById(R.id.b_introducao)
        botaoIntroducao.setOnClickListener{
            GoIntroducao()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun GoIntroducao(){
        // Variável de Intent por convenção com 'i' minúsculo
        val intentIntroducao = Intent(this,Introducao::class.java)
        startActivity(intentIntroducao)

    }
}