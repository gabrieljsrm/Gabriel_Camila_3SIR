package com.example.gabriel_camila_3sir.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gabriel_camila_3sir.databinding.ActivityFormBinding
import com.example.gabriel_camila_3sir.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private lateinit var formAdapter: FormAdapter
    private val formularios = mutableListOf<Formulario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        formAdapter = FormAdapter(formularios)
        binding.recyclerViewForm.adapter = formAdapter
        binding.recyclerViewForm.layoutManager = LinearLayoutManager(this)

        carregarDados()

        binding.btnSubmit.setOnClickListener {
            obterTemperaturaAtualESalvarFormulario()
        }
    }

    private fun obterTemperaturaAtualESalvarFormulario() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        weatherService.getCurrentWeather().enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val temperatura = response.body()?.main?.temp ?: 0f
                    val novoFormulario = coletarDadosFormulario(temperatura)
                    novoFormulario?.let {
                        formularios.add(it)
                        formAdapter.notifyDataSetChanged()
                        salvarDados(it)
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@FormActivity, "Erro ao obter temperatura", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun coletarDadosFormulario(temperatura: Float): Formulario? {
        val nomePaciente = binding.etNomePaciente.text.toString()
        val nivelDor = binding.etNivelDor.text.toString().toIntOrNull()
        val conforto = binding.etConforto.text.toString().toIntOrNull()
        val fadiga = binding.etFadiga.text.toString().toIntOrNull()
        val qualidadeSono = binding.etQualidadeSono.text.toString().toIntOrNull()
        val apetite = binding.etApetite.text.toString().toIntOrNull()
        val batimentosCardiacos = binding.etBatimentos.text.toString().toIntOrNull()
        val pressaoArterial = if (binding.etPressao.text.toString().isEmpty()) "120/80" else binding.etPressao.text.toString()

        if (!validarValor(nivelDor) || !validarValor(conforto) || !validarValor(fadiga) || !validarValor(qualidadeSono) || !validarValor(apetite) || !validarBatimentos(batimentosCardiacos) || !validarPressao(pressaoArterial)) {
            Toast.makeText(this, "Valores inválidos. Por favor, verifique os dados.", Toast.LENGTH_SHORT).show()
            return null
        }

        val paciente = Paciente(id = "1", nome = nomePaciente, idade = 30)
        val avaliacaoSaude = AvaliacaoSaude(
            nivelDor = nivelDor!!,
            conforto = conforto!!,
            fadiga = fadiga!!,
            qualidadeSono = qualidadeSono!!,
            apetite = apetite!!,
            batimentosCardiacos = batimentosCardiacos!!,
            pressaoArterial = pressaoArterial,
            temperaturaAtual = temperatura
        )

        return Formulario(paciente, avaliacaoSaude, Data(data = LocalDate.now()))
    }

    private fun salvarDados(formulario: Formulario) {
        val sharedPreferences = getSharedPreferences("FormularioPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("nomePaciente", formulario.paciente.nome)
        editor.putInt("nivelDor", formulario.avaliacaoSaude.nivelDor)
        editor.putInt("conforto", formulario.avaliacaoSaude.conforto)
        editor.putInt("fadiga", formulario.avaliacaoSaude.fadiga)
        editor.putInt("qualidadeSono", formulario.avaliacaoSaude.qualidadeSono)
        editor.putInt("apetite", formulario.avaliacaoSaude.apetite)
        editor.putInt("batimentosCardiacos", formulario.avaliacaoSaude.batimentosCardiacos)
        editor.putString("pressaoArterial", formulario.avaliacaoSaude.pressaoArterial)
        editor.putFloat("temperaturaAtual", formulario.avaliacaoSaude.temperaturaAtual)

        editor.apply()
    }

    private fun carregarDados() {
        val sharedPreferences = getSharedPreferences("FormularioPrefs", MODE_PRIVATE)

        val nomePaciente = sharedPreferences.getString("nomePaciente", "")
        val nivelDor = sharedPreferences.getInt("nivelDor", 0)
        val conforto = sharedPreferences.getInt("conforto", 0)
        val fadiga = sharedPreferences.getInt("fadiga", 0)
        val qualidadeSono = sharedPreferences.getInt("qualidadeSono", 0)
        val apetite = sharedPreferences.getInt("apetite", 0)
        val batimentosCardiacos = sharedPreferences.getInt("batimentosCardiacos", 0)
        val pressaoArterial = sharedPreferences.getString("pressaoArterial", "") ?: "120/80"
        val temperaturaAtual = sharedPreferences.getFloat("temperaturaAtual", 0f)

        if (!nomePaciente.isNullOrEmpty()) {
            val paciente = Paciente(id = "1", nome = nomePaciente, idade = 30)
            val avaliacaoSaude = AvaliacaoSaude(
                nivelDor = nivelDor,
                conforto = conforto,
                fadiga = fadiga,
                qualidadeSono = qualidadeSono,
                apetite = apetite,
                batimentosCardiacos = batimentosCardiacos,
                pressaoArterial = pressaoArterial,
                temperaturaAtual = temperaturaAtual
            )
            val formulario = Formulario(paciente, avaliacaoSaude, Data(data = LocalDate.now()))

            formularios.add(formulario)
            formAdapter.notifyDataSetChanged()
            preencherFormulario(formulario)
        }
    }
    private fun preencherFormulario(formulario: Formulario) {
        binding.etNomePaciente.setText(formulario.paciente.nome)
        binding.etNivelDor.setText(formulario.avaliacaoSaude.nivelDor.toString())
        binding.etConforto.setText(formulario.avaliacaoSaude.conforto.toString())
        binding.etFadiga.setText(formulario.avaliacaoSaude.fadiga.toString())
        binding.etQualidadeSono.setText(formulario.avaliacaoSaude.qualidadeSono.toString())
        binding.etApetite.setText(formulario.avaliacaoSaude.apetite.toString())
        binding.etBatimentos.setText(formulario.avaliacaoSaude.batimentosCardiacos.toString())
        binding.etPressao.setText(formulario.avaliacaoSaude.pressaoArterial)
        binding.etTemperatura.setText(formulario.avaliacaoSaude.temperaturaAtual.toString()) // Adicione esta linha
    }



    private fun validarValor(valor: Int?): Boolean {
        return valor != null && valor in 0..10
    }

    private fun validarBatimentos(batimentos: Int?): Boolean {
        return batimentos != null && batimentos in 0..300
    }

    private fun validarPressao(pressao: String?): Boolean {
        val padraoPressao = Regex("^\\d{2,3}/\\d{2}$")
        return pressao != null && pressao.matches(padraoPressao)
    }
}
