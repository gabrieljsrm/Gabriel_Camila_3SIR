package com.example.gabriel_camila_3sir.model

import retrofit2.Call
import retrofit2.http.GET

interface WeatherService {
    @GET("data/2.5/weather?q=Sao+Paulo&units=metric&appid=e4a86aebd7636a6083263bba49092ad7")
    fun getCurrentWeather(): Call<WeatherResponse>
}
