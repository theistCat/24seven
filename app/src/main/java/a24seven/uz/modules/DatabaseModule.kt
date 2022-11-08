package a24seven.uz.modules

import org.koin.dsl.module
import a24seven.uz.network.CartDao
import a24seven.uz.network.CartDatabase

val databaseModule = module {

    single<CartDao> {
        val database = get<CartDatabase>()
        database.cartDao()
    }

    single<CartDatabase> {
        CartDatabase.getDatabase(get())
    }
}