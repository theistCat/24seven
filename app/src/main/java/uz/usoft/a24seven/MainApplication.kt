package uz.usoft.a24seven

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.usoft.a24seven.modules.*
import uz.usoft.a24seven.network.utils.ConnectivityMonitor
import uz.usoft.a24seven.network.utils.Variables

class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        mContext=this@MainApplication
        startKoin {
            androidLogger(org.koin.core.logger.Level.ERROR)
            androidContext(this@MainApplication)
            modules(listOf(appModule, viewModelsModule, networkModule))

            //Start network callback
        }
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