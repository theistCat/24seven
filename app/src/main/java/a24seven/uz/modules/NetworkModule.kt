package a24seven.uz.modules

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import a24seven.uz.BuildConfig
import a24seven.uz.network.SevenApi
import a24seven.uz.network.utils.ConnectivityInterceptor
import a24seven.uz.data.PrefManager
import a24seven.uz.network.utils.LogoutInterceptor
import java.util.concurrent.TimeUnit


private const val BASE_URL: String = "http://api.24seven.uz/"
//"http://192.168.31.212:8002/"

val networkModule = module {

    single<SevenApi> {
        val retrofit = get<Retrofit>()
        retrofit.create(SevenApi::class.java)
    }


    single<Moshi> {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ConnectivityInterceptor())
            .addInterceptor(LogoutInterceptor(get()))
            .addInterceptor { chain ->
                // val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9hcGktc2V2ZW4udXNvZnRkZXYudXpcL1wvb2F1dGhcL3ZlcmlmeSIsImlhdCI6MTYxNzQyODI4NCwiZXhwIjoxNjQ4OTY0Mjg0LCJuYmYiOjE2MTc0MjgyODQsImp0aSI6Ikpmd1B3ZnVSOWhDbGdLWVMiLCJzdWIiOjYsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.xt0S-qmV3aSUNdn6hFrtPmRIVnVODmYaFqSdNzZaaos"
                val token = PrefManager.getToken(get())
                val locale: String = PrefManager.getLocale(get())
                try {
                    val request = chain.request().newBuilder()
                    request.addHeader("Content-type", "application/json")
                    request.addHeader("X-Requested-With", "XMLHttpRequest")
                    if (token != "")
                        request.addHeader("Authorization", "Bearer $token")
                    if (locale != "")
                        request.addHeader("Accept-Language", locale)
                    else
                        request.addHeader("x", "ru")

                    return@addInterceptor chain.proceed(request.build())
                } catch (e: Throwable) {

                }
                return@addInterceptor chain.proceed(chain.request())
            }
        //If debugged version, network request debugger added
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(ChuckerInterceptor(get()))
        }
        clientBuilder.build()
    }

}