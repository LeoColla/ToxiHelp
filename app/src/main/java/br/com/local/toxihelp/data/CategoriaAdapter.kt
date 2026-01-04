package br.com.local.toxihelp.data

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.local.toxihelp.R
import br.com.local.toxihelp.domain.Categoria

class CategoriaAdapter(
    private val onItemClick : (Categoria) -> Unit
): ListAdapter<Categoria, CategoriaAdapter.CategoriaViewHolder>(CategoriaDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_categorias, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = getItem(position)
        holder.bind(categoria)
        holder.itemView.setOnClickListener {
            onItemClick(categoria)
        }
    }
    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titulo: TextView = itemView.findViewById(R.id.categoria_titulo)
        private val container: RelativeLayout = itemView.findViewById(R.id.RL_categoria)

        fun bind(categoria: Categoria) {
            titulo.text = categoria.nome

            val cor = try {
                categoria.colorCode.toColorInt()
            } catch (e: Exception) {
                Color.GRAY // fallback
            }
            container.setBackgroundColor(cor)
        }
    }
}

// Objeto para calcular a diferença entre a lista antiga e a nova
// Classe do Android para fazer especificamente isso
object CategoriaDiffCallback : DiffUtil.ItemCallback<Categoria>() {
    override fun areItemsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
        // nome é um identificador único
        return oldItem.nome == newItem.nome
    }

    override fun areContentsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
        return oldItem == newItem
    }
}