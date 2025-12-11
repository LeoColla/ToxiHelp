package br.com.local.toxihelp.data

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.local.toxihelp.R
import br.com.local.toxihelp.data.local.entity.EntidadeResumo

class EntidadeToxicaAdapter (
    private val onItemClick : (EntidadeResumo) -> Unit
): ListAdapter<EntidadeResumo, EntidadeToxicaAdapter.EntidadeToxicaViewHolder>(EntidadeToxicaDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntidadeToxicaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_entidades_toxicas, parent, false)
        return EntidadeToxicaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntidadeToxicaViewHolder, position: Int) {
        val entidade = getItem(position)
        holder.bind(entidade)
        holder.itemView.setOnClickListener {
            onItemClick(entidade)
        }
    }
    class EntidadeToxicaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nome: TextView = itemView.findViewById(R.id.entidade_nome)
        private val container: RelativeLayout = itemView.findViewById(R.id.lL_entidade_toxica)

        fun bind(entidade: EntidadeResumo) {
            nome.text = entidade.nomePopular

            // podemos deixar cores dinamicas
            val cor = Color.GRAY
            container.setBackgroundColor(cor)
        }
    }
}

object EntidadeToxicaDiffCallback : DiffUtil.ItemCallback<EntidadeResumo>() {
    override fun areItemsTheSame(oldItem: EntidadeResumo, newItem: EntidadeResumo): Boolean {
        return oldItem.nomePopular == newItem.nomePopular
    }

    override fun areContentsTheSame(oldItem: EntidadeResumo, newItem: EntidadeResumo): Boolean {
        return oldItem == newItem
    }
}