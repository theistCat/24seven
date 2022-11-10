package uz.usoft.a24seven.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import a24seven.uz.ui.category.CategoriesViewModel
import a24seven.uz.ui.home.HomeViewModel

val viewModelsModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { CategoriesViewModel(get()) }
}