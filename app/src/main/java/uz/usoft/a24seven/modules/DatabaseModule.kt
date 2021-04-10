package uz.usoft.a24seven.modules

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uz.usoft.a24seven.network.CartDao
import uz.usoft.a24seven.network.CartDatabase
import uz.usoft.a24seven.network.SevenApi

val databaseModule = module {

    single<CartDao> {
        val database = get<CartDatabase>()
        database.cartDao()
    }

    single<CartDatabase> {
        CartDatabase.getDatabase(get())
    }
}