package com.application.seb.binfinder.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.application.seb.binfinder.R
import com.application.seb.binfinder.controllers.activities.MainActivity
import com.application.seb.binfinder.models.CleanEvent
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

const val TAG = "Utils"

class Utils {

    companion object {

//--------------------------------------------------------------------------------------------------
// Converters
//--------------------------------------------------------------------------------------------------
        @JvmStatic
        fun convertLatLngToString(location: LatLng): String {
            val gSon = Gson()
            return gSon.toJson(location)
        }

        @JvmStatic
        fun convertStringToLatLng(location: String): LatLng{
            val propertyType = object : TypeToken<LatLng>() {}.type
            return Gson().fromJson(location, propertyType)

        }

        @JvmStatic
        fun convertGeoPointToString(geoPoint: GeoPoint):String{
            val gSon = Gson()
            return gSon.toJson(geoPoint)
        }

        @JvmStatic
        fun convertStringToGeoPoint(sGeoPoint: String):GeoPoint{
            val geoType = object : TypeToken<GeoPoint>(){}.type
            return Gson().fromJson(sGeoPoint, geoType)
        }

        @JvmStatic
        fun convertCleanEventToString(event: CleanEvent):String{
            val gSon = Gson()
            return gSon.toJson(event)
        }

        @JvmStatic
        fun convertStringToCleanEvent(sCleanEvent: String):CleanEvent{
            val eventType = object : TypeToken<CleanEvent>(){}.type
            return Gson().fromJson(sCleanEvent, eventType)
        }

        @JvmStatic
        fun convertCalendarToFormatString(calendar: Calendar): String? {
            val dayOfMonth = convertDateIntToString(calendar[Calendar.DAY_OF_MONTH])
            val month = convertDateIntToString(calendar[Calendar.MONTH] + 1)
            val year = calendar[Calendar.YEAR].toString()
            Log.e(TAG, "format date : $year$month$dayOfMonth")
            return "$year$month$dayOfMonth"
        }

        @JvmStatic
        fun convertDateIntToString(date: Int): String {
            return if (date < 10) {
                "0$date"
            } else {
                date.toString()
            }
        }

        @JvmStatic
        fun convertDataDateToFormatString(date: Int): String? {

            val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val newSdf = SimpleDateFormat("dd / MM / yyyy", Locale.getDefault())
            val mDate : Date = sdf.parse(date.toString())!!
            val formatDate = newSdf.format(mDate)
            Log.e(TAG, " convertDataDateToFormatString($mDate) = $formatDate ")
            return formatDate

        }


//--------------------------------------------------------------------------------------------------
// Notification
//--------------------------------------------------------------------------------------------------
        @JvmStatic
        fun startNotification(title: String?, messageContent: String?, context: Context) {

        val resultIntent = Intent(context, MainActivity::class.java)
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val resultPendingIntent = PendingIntent.getActivity(context, 0,
                    resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val mBuilder = NotificationCompat.Builder(context, Constants.CHANNEL_1_ID)
            mBuilder.setSmallIcon(R.drawable.ic_map)
                    .setContentTitle(title)
                    .setContentText(messageContent)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent)
            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(Constants.CHANNEL_1_ID, Constants.CHANNEL_1_NAME, importance)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mBuilder.setChannelId(Constants.CHANNEL_1_ID)
                Objects.requireNonNull(mNotificationManager).createNotificationChannel(notificationChannel)
            }
            Objects.requireNonNull(mNotificationManager).notify(0, mBuilder.build())
        }
    }
}