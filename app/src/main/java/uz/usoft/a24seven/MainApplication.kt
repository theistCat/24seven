package uz.usoft.a24seven

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
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

    lateinit var channel: NotificationChannel


    lateinit var mgr : NotificationManager


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

        FirebaseApp.initializeApp(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                getString(R.string.call_notification_channel_id), "New notification",
                NotificationManager.IMPORTANCE_HIGH
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           mgr=getSystemService(NotificationManager::class.java)
        } else {
           mgr= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(this::mgr.isInitialized)
                mgr.createNotificationChannel(channel)
        }

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