package br.com.local.toxihelp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color
import br.com.local.toxihelp.domain.ICategoria
import androidx.core.graphics.toColorInt

class CategoriaAdapter(
    private val categorias: List<ICategoria>,
    private val onItemClick : (ICategoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_categorias, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categorias[position]
        holder.bind(categoria)
        holder.itemView.setOnClickListener { onItemClick(categoria) } // Define o click listener
    }

    override fun getItemCount(): Int = categorias.size

    class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titulo: TextView = itemView.findViewById(R.id.tv_categoria_titulo)
        private val container: RelativeLayout = itemView.findViewById(R.id.RL_categoria)
        //private val intro: TextView = itemView.findViewById(R.id.item_pontuacao_desafio)
        //private val itens: TextView = itemView.findViewById(R.id.item_periodo_desafio)

        fun bind(categoria: ICategoria) {
            titulo.text = categoria.nome
            //intro.text = categoria.intro

            val cor = try {
                categoria.colorCode.toColorInt()
            } catch (e: Exception) {
                Color.GRAY // fallback
            }
            container.setBackgroundColor(cor)
            //itens.text = categoria.itens
        }
    }
}