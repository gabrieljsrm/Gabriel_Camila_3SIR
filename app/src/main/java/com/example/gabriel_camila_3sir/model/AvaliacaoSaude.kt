package com.example.gabriel_camila_3sir.model

data class AvaliacaoSaude(
    val nivelDor: Int,
    val conforto: Int,
    val fadiga: Int,
    val qualidadeSono: Int,
    val apetite: Int,
    val batimentosCardiacos: Int,
    val pressaoArterial: String,
    val temperaturaAtual: Float // Adicione este campo
)
