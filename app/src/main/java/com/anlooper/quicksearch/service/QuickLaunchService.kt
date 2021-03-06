package com.anlooper.quicksearch.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.anlooper.quicksearch.broadcast.QuickLaunchBroadcastReceiver
import com.anlooper.quicksearch.service.listener.QuickLaunchBRListener
import com.anlooper.quicksearch.view.window.WindowView
import com.anlooper.quicksearch.view.window.presenter.WindowViewPresenter
import tech.thdev.base.util.registerReceiverAction
import tech.thdev.base.util.startServiceClass

/**
 * Created by tae-hwan on 8/17/16.
 *
 * WindowManager app control.
 */
class QuickLaunchService : Service() {

    private val quickLaunchBroadcastReceiver = QuickLaunchBroadcastReceiver()

    var windowView: WindowView? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        startForeground()
        startServiceClass(SampleService::class.java)

        registerReceiverAction(quickLaunchBroadcastReceiver, listOf(Intent.ACTION_SCREEN_OFF, Intent.ACTION_SCREEN_ON, Intent.ACTION_USER_PRESENT))

        val windowView = WindowView(this)
        windowView.showWindowView()
        WindowViewPresenter().attachView(windowView)

        quickLaunchBroadcastReceiver.brListener = object : QuickLaunchBRListener {
            override fun actionUpdate(action: String?) {
                action?.let {
                    Toast.makeText(this@QuickLaunchService, it, Toast.LENGTH_SHORT).show()

                    when (it) {
                        Intent.ACTION_USER_PRESENT -> {
//                            windowView.hideWindowView()
                        }
                        Intent.ACTION_SCREEN_ON -> {
//                            windowView.showWindowView()
                        }
                        Intent.ACTION_SCREEN_OFF -> {

                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TAG", "onDestroy service")
        unregisterReceiver(quickLaunchBroadcastReceiver)
        windowView?.onDestroy()
    }


}