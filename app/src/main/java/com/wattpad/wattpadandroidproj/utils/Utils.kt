package com.wattpad.wattpadandroidproj.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.wattpad.wattpadandroidproj.R

class Utils private constructor() {
    private var context: Context? = null

    companion object {
        private var INSTANCE: Utils? = null
        fun getInstance(context: Context): Utils {
            if (INSTANCE == null) {
                INSTANCE = Utils()
                INSTANCE?.context = context
            }
            return INSTANCE!!
        }
    }

    fun loadImage(context: Context, url: String, view: ImageView) {
        Glide.with(context.applicationContext)
            .load(url)
            .placeholder(R.drawable.ic_panorama)
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_broken_image)
            .into(view);
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun isInternetAvailable(): Boolean {
        val connMgr = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifiConn = false
        var isMobileConn = false
        for (network in connMgr.allNetworks) {
            val networkInfo = connMgr.getNetworkInfo(network)
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                isWifiConn = isWifiConn or networkInfo.isConnected
            }
            if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn = isMobileConn or networkInfo.isConnected
            }
        }
        return isWifiConn || isMobileConn
    }

    fun openDeviceConnectionSetting() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }

    fun showSnackBar(
        contextView: View?,
        msg: String?,
        duration: Int,
        actionTitle: String?,
        actionListener: View.OnClickListener?
    ): Snackbar? {
        val snackbar = Snackbar.make(contextView!!, msg!!, duration)
        if (actionListener != null) {
            snackbar.setAction(actionTitle, actionListener)
        }
        snackbar.show()
        return snackbar
    }

    fun cancelSnackBar(snackbar: Snackbar?) {
        snackbar?.dismiss()
    }
}