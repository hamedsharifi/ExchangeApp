package ir.moonify.exchangeapp

import ir.moonify.exchangeapp.model.ExchangeData
import ir.moonify.exchangeapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitService {

    @GET("/v1/latest")
    suspend fun getExchangeData(@Query("access_key") accessKey: String) : Response<ExchangeData>

    companion object {
        var retrofitService: RetrofitService? = null
        var okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(provideHttpLoggingInterceptor())
        .build()
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {

            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }


}