package uz.rakhmonov.restapiretrofit.network

import android.content.Context
import android.net.ConnectivityManager

class MyNetworkHelper {
    fun hasConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiInfo != null && wifiInfo.isConnected) {
            true
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifiInfo != null && wifiInfo.isConnected) {
            true
        }
        wifiInfo = cm.activeNetworkInfo
        return (wifiInfo != null) && wifiInfo.isConnected

    }
}