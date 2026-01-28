package br.com.local.toxihelp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.local.toxihelp.R
import br.com.local.toxihelp.data.local.entity.ElementoResumo

class ElementosAdapter (
    private val onItemClick : (ElementoResumo) -> Unit
): ListAdapter<ElementoResumo, ElementosAdapter.ElementosViewHolder>(ElementosDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_elementos, parent, false)
        return ElementosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElementosViewHolder, position: Int) {
        val entidade = getItem(position)
        holder.bind(entidade)
        holder.itemView.setOnClickListener {
            onItemClick(entidade)
        }
    }
    class ElementosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nome: TextView = itemView.findViewById(R.id.elemento_resumo_nome)
        // Pegamos o CardView para mudar a cor de fundo dele
        //private val card: androidx.cardview.widget.CardView = itemView as androidx.cardview.widget.CardView

        fun bind(elemento: ElementoResumo) {
            // 1. Define o nome do elemento
            nome.text = elemento.nomePopular

            /*
            // 2. Busca a cor "vermelho" do arquivo colors.xml e aplica ao fundo do CARD
            val corBotao = androidx.core.content.ContextCompat.getColor(itemView.context, R.color.branco)
            card.setCardBackgroundColor(corBotao)

            // 3. Busca a cor "branco" do arquivo colors.xml e aplica ao texto
            val corTexto = androidx.core.content.ContextCompat.getColor(itemView.context, R.color.vermelho_escuro)
            nome.setTextColor(corTexto)
            */
        }
    }
}

object ElementosDiffCallback : DiffUtil.ItemCallback<ElementoResumo>() {
    override fun areItemsTheSame(oldItem: ElementoResumo, newItem: ElementoResumo): Boolean {
        return oldItem.nomePopular == newItem.nomePopular
    }

    override fun areContentsTheSame(oldItem: ElementoResumo, newItem: ElementoResumo): Boolean {
        return oldItem == newItem
    }
}