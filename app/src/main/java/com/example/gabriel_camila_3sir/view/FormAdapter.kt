package com.example.gabriel_camila_3sir.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gabriel_camila_3sir.model.Formulario
import com.example.gabriel_camila_3sir.databinding.ItemFormularioBinding

class FormAdapter(private val formularios: List<Formulario>) : RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    class FormViewHolder(val binding: ItemFormularioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(formulario: Formulario) {
            with(binding) {
                textViewNomePaciente.text = formulario.paciente.nome
                textViewNivelDor.text = "Dor: ${formulario.avaliacaoSaude.nivelDor}"
                textViewConforto.text = "Conforto: ${formulario.avaliacaoSaude.conforto}"
                textViewFadiga.text = "Fadiga: ${formulario.avaliacaoSaude.fadiga}"
                textViewQualidadeSono.text = "Sono: ${formulario.avaliacaoSaude.qualidadeSono}"
                textViewApetite.text = "Apetite: ${formulario.avaliacaoSaude.apetite}"
                textViewBatimentos.text = "Batimentos: ${formulario.avaliacaoSaude.batimentosCardiacos}"
                textViewTemperatura.text = "Temperatura: ${formulario.avaliacaoSaude.temperaturaAtual}"
                textViewPressao.text = "Pressão: ${formulario.avaliacaoSaude.pressaoArterial}"

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFormularioBinding.inflate(layoutInflater, parent, false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        holder.bind(formularios[position])
    }

    override fun getItemCount() = formularios.size
}
