package uz.usoft.a24seven.network.utils



import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.usoft.a24seven.R


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

       if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message notification body: ${remoteMessage.notification!!.body}")
            val CHANNEL_ID=this.getString(R.string.call_notification_channel_id)

            val notification: Notification =
                NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(remoteMessage.notification!!.title)
                    .setContentText(remoteMessage.notification!!.body)
                    .setSmallIcon(R.drawable.ic_logo_small)
                    .build()

            val mNotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            with(mNotificationManager) {
                notify(0, notification)
            }
        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.



    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Refreshed token $token")
       // PrefManager.saveFirebaseToken(this, token)
        CoroutineScope(Dispatchers.IO).launch {
//            if (isNetworkConnectedSuspend()) {
//                repository.sendFirebaseToken(
//                    token
//                )
//            }
        }
    }

}