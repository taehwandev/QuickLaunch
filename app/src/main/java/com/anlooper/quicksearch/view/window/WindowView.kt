package com.anlooper.quicksearch.view.window

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.anlooper.quicksearch.R
import com.anlooper.quicksearch.view.window.presenter.WindowViewContract
import tech.thdev.base.util.addWindowView

/**
 * Created by Tae-hwan on 8/18/16.
 */
class WindowView(val context: Context) : WindowViewContract.View {

    private var presenter: WindowViewContract.Presenter? = null

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private var windowViewLayoutParams: WindowManager.LayoutParams? = null
    private var windowView: View? = null

    override fun onPresenter(presenter: WindowViewContract.Presenter) {
        this.presenter = presenter
    }

    init {
        onCreateView()
    }

    private fun onCreateView() {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        windowView = layoutInflater.inflate(R.layout.window_item_view, null)
        windowViewLayoutParams = windowManager.addWindowView(windowView, 30, 30)
        windowView?.setOnTouchListener { view, motionEvent ->
            presenter?.onTouch(motionEvent) ?: true
        }
        windowView?.visibility = View.GONE
    }

    /**
     * Show window view...
     */
    fun showWindowView() {
        windowView?.visibility = View.VISIBLE
    }

    /**
     * Hide window view
     */
    fun hideWindowView() {
        windowView?.visibility = View.GONE
    }

    override fun onObtainingPermissionOverlayWindow() {
//        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
//        context.startActivity
    }

    override fun onStartOverlay() {
        windowViewLayoutParams = windowManager.addWindowView(windowView, 30, 30)
    }

    override fun updateViewLayout(x: Int, y: Int) {
        windowViewLayoutParams?.x = windowViewLayoutParams?.x?.plus(x)
        windowViewLayoutParams?.y = windowViewLayoutParams?.y?.plus(y)

        windowManager.updateViewLayout(windowView, windowViewLayoutParams)
    }

    fun onDestroy() {
        presenter?.detachView()
        presenter?.destroy()
    }
}

