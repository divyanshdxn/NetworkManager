package com.example.myapplication

import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.connectivity.ConnectivityProviderImpl
import com.example.myapplication.connectivity.base.ConnectivityProvider
import com.example.myapplication.connectivity.base.ConnectivityProvider.ConnectivityStateListener
import com.example.myapplication.connectivity.base.ConnectivityProvider.NetworkState
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ConnectivityStateListener {


    private lateinit var provider: ConnectivityProvider
    private lateinit var wifi: WifiManager
    private lateinit var telephoneService: TelephonyManager

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wifi = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        provider = ConnectivityProviderImpl(cm)
        provider.addListener(this)


        binding.button.setOnClickListener {
            wifi.disconnect()
            Log.d("TAG", "onStateChange: ${wifi.isWifiEnabled}")
        }
    }


    override fun onStop() {
        super.onStop()
        provider.removeListener(this)
    }

    override fun onStateChange(state: NetworkState) {
        val hasInternet = state.hasInternet()
        binding.statusText.text = "Connectivity (via callback): $hasInternet"
        if (hasInternet) {
            Log.d("TAG", "onStateChange: enabled")
            wifi.setWifiEnabled(false)
        }
    }

    private fun NetworkState.hasInternet(): Boolean {
        return (this as? NetworkState.ConnectedState)?.hasInternet == true
    }

}