package com.wattpad.wattpadandroidproj.view.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wattpad.wattpadandroidproj.R

class SplashActivity : AppCompatActivity() {
    private val DURATION: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkRequirePermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissionStatus in grantResults) {
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                this@SplashActivity.finish()
                return
            }
        }
        goNextActivity()
    }

    private fun checkRequirePermissions() {
        val permissions: Array<String> = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                showPermissionDialog(permissions)
                return
            }
        }
        goNextActivity()
    }

    private fun showPermissionDialog(permissions: Array<String>) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.permission_title))
            .setMessage(resources.getString(R.string.permission_msg))
            .setPositiveButton(resources.getString(R.string.agree))
            { dialog: DialogInterface?, which: Int ->
                ActivityCompat.requestPermissions(this@SplashActivity, permissions, 0)
            }
            .setNegativeButton(resources.getString(R.string.disagree))
            { dialog: DialogInterface?, which: Int ->
                finish()
            }
            .show()
    }

    private fun goNextActivity() {
        Handler().postDelayed({
            val intent = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            this@SplashActivity.finish()
        }, DURATION)
    }


    override fun onBackPressed() {
    }
}