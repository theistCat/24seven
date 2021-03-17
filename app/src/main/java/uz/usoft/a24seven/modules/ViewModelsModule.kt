package uz.usoft.a24seven.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.usoft.a24seven.ui.home.HomeViewModel

val viewModelsModule = module {
    viewModel { HomeViewModel(get()) }
}