package uz.usoft.a24seven.modules

import org.koin.dsl.module
import uz.usoft.a24seven.repository.SevenRepository

val appModule= module{
    single {  SevenRepository(get(),get())}
}