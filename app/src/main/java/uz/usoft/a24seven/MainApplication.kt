package uz.usoft.a24seven

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.usoft.a24seven.modules.*

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(org.koin.core.logger.Level.ERROR)
            androidContext(this@MainApplication)
            modules(listOf(appModule, viewModelsModule, networkModule))
        }

    }
}