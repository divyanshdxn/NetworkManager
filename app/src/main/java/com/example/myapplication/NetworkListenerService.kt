package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import com.example.myapplication.connectivity.ConnectivityProviderImpl
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.connectivity.base.ConnectivityProvider.ConnectivityStateListener
import com.example.myapplication.connectivity.base.ConnectivityProvider.NetworkState


class NetworkListenerService : Service(), ConnectivityStateListener {

    private lateinit var wifi: WifiManager
    private lateinit var provider: ConnectivityProvider


    override fun onCreate() {
        super.onCreate()
        wifi = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        provider = ConnectivityProviderImpl(cm)
        provider.addListener(this)
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            while (true) {
                Log.e("Service", "Service is running...")
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onStateChange(state: NetworkState) {
        val hasInternet = state.hasInternet()
        if (hasInternet) {
            wifi.setWifiEnabled(false)
        }
    }


    private fun NetworkState.hasInternet(): Boolean {
        return (this as? NetworkState.ConnectedState)?.hasInternet == true
    }
}