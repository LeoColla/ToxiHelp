package br.com.local.toxihelp.data

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
        private val container: LinearLayout = itemView.findViewById(R.id.lL_elemento_resumo)

        fun bind(elemento: ElementoResumo) {
            nome.text = elemento.nomePopular

            // podemos deixar cores dinamicas
            val cor = Color.GRAY
            container.setBackgroundColor(cor)
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