package fr.group5.magellangpt.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.java.KoinJavaComponent.get
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val preferencesHelper : PreferencesHelper = get(PreferencesHelper::class.java)

    val apiService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val gson : Gson by lazy {
        GsonBuilder()
            .setLenient()
            .create()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://di3-p3-test-api.azurewebsites.net/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val httpClient : OkHttpClient by lazy {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    private val headerInterceptor = Interceptor { chain ->
        val original = chain.request()

        val requestBuilder = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer ${preferencesHelper.accessToken}")

        val request = requestBuilder.build()
        chain.proceed(request)
    }


}