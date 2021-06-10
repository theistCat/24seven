package uz.usoft.a24seven

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.modules.*
import uz.usoft.a24seven.network.utils.ConnectivityMonitor
import uz.usoft.a24seven.network.utils.Variables

class MainApplication : Application() {

    //TODO: alias : key0   password : a24sevenkey

    override fun onCreate() {
        super.onCreate()
        mContext=this@MainApplication
        startKoin {
            androidLogger(org.koin.core.logger.Level.ERROR)
            androidContext(this@MainApplication)
            modules(listOf(appModule, viewModelsModule, networkModule, databaseModule))

            //Start network callback
        }
        if(PrefManager.getTheme(this)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        MapKitFactory.setApiKey(Variables.yandexKey)
        ConnectivityMonitor(this).startNetworkCallback()

    }


    override fun onTerminate(){
        super.onTerminate()
        //Stop network callback
        ConnectivityMonitor(this).stopNetworkCallback()
    }
    companion object{
        lateinit var mContext : Context

        fun getContext() : Context
        {
            return mContext
        }
    }

}