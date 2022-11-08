package a24seven.uz.modules

import org.koin.dsl.module
import a24seven.uz.repository.SevenRepository

val appModule= module{
    single {  SevenRepository(get(),get()) }
}