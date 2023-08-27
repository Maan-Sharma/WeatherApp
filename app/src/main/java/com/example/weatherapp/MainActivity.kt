package com.example.weatherapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

//d44d50dd6f36c38f0787d914036c55b1
class MainActivity : AppCompatActivity() {
private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        fatchweatherData("Kolkata")
        searchCity()
    }

    private fun searchCity() {
        val searchview = binding.searchView
        searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fatchweatherData(query)
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
           return true
            }

        })
    }

    private fun fatchweatherData(CityName:String){
        val retrofit =Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(Apiinterface::class.java)
        val responce = retrofit.getWeatherData( CityName, "d44d50dd6f36c38f0787d914036c55b1","metric")
        responce.enqueue(object : Callback<Weatherapp>{
            override fun onResponse(call: Call<Weatherapp>, response: Response<Weatherapp>) {
                val responseBody =response.body()
                if(response.isSuccessful && responseBody !=null){
                    val temperature =responseBody.main.temp.toString()
                    val Maxtemp=responseBody.main.temp_max.toString()
                    val Mintemp=responseBody.main.temp_min.toString()
                    val himudity=responseBody.main.humidity
                    val windspeed=responseBody.wind.speed
                    val sunshine = responseBody.sys.sunrise
                    val sunset = responseBody.sys.sunset
                    val sealevel=responseBody.main.pressure
                    val condition =responseBody.weather.firstOrNull()?.main?:"UnKnown"



                    binding.tempratuer.text= "$temperature°C"
                    binding.maxtemp.text="Max Temp $Maxtemp°C"
                    binding.mintemp.text="Min Temp $Mintemp°C"
                    binding.weather.text=condition
                    binding.Humadity.text="$himudity %"
                    binding.windspped.text="$windspeed m/s"
                    binding.sunries.text="$sunshine"
                    binding.sunset.text="$sunset"
                    binding.sea.text="$sealevel"
                    binding.Condition.text=condition
                    binding.Day.text=DayName(System.currentTimeMillis())
                        binding.Date.text=date()
                        binding.cityname.text="$CityName"



                   // Log.d("TAG","Onresponce: $temperature")
                    changeImageaccrodingtheweatherCondition(condition)
                }
            }

            override fun onFailure(call: Call<Weatherapp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun changeImageaccrodingtheweatherCondition(condition:String) {
        when(condition){
            "Sunny"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Clear Sky","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sun)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Haze","Foggy"->{
                binding.root.setBackgroundResource(R.drawable.fog)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Overcast"->{
                binding.root.setBackgroundResource(R.drawable.over)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Mist"->{
                binding.root.setBackgroundResource(R.drawable.mist)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Partly Clouds","Clouds"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    fun DayName (timestamp: Long):String{
        val sdf= SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

   private fun date():String{
        val sdf= SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))}



}
