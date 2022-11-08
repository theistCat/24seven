package a24seven.uz.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import a24seven.uz.MainViewModel
import a24seven.uz.ui.auth.AuthViewModel
import a24seven.uz.ui.cart.CartViewModel
import a24seven.uz.ui.category.CategoriesViewModel
import a24seven.uz.ui.checkout.CheckoutViewModel
import a24seven.uz.ui.coin.CoinViewModel
import a24seven.uz.ui.products.ProductViewModel
import a24seven.uz.ui.home.HomeViewModel
import a24seven.uz.ui.map.MapViewModel
import a24seven.uz.ui.news.NewsViewModel
import a24seven.uz.ui.profile.ProfileViewModel
import a24seven.uz.ui.profile.myOrders.OrdersViewModel
import a24seven.uz.ui.seach.SearchViewModel

val viewModelsModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { CategoriesViewModel(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CartViewModel(get()) }
    viewModel { CheckoutViewModel(get()) }
    viewModel { OrdersViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MapViewModel() }
    viewModel { CoinViewModel(get()) }
}