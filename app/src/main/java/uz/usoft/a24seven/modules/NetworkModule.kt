package uz.usoft.a24seven.modules

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uz.usoft.a24seven.BuildConfig
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.kidya.data.PrefManager
import java.util.concurrent.TimeUnit


private const val BASE_URL: String = "http://api-seven.usoftdev.uz/"

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
            //     .addInterceptor(LogoutInterceptor(get()))
            .addInterceptor { chain ->
                val token = " "
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
                        request.addHeader("Accept-Language", "ru")

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