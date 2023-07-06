package com.online.bk.olimpbet.presentation.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.util.Base64
import android.view.*
import android.webkit.*
import android.webkit.WebView.WebViewTransport
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.online.bk.olimpbet.R
import com.online.bk.olimpbet.data.helper.HISTORY_KEY
import com.online.bk.olimpbet.data.helper.px
import com.online.bk.olimpbet.presentation.view_model.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class mnbvcerteyru67687tre5wghe6jr75kERTRYUIudysg : Fragment() {

    private val sharedVm by viewModel<SharedViewModel>()

    private var fileChooserValueCallback: ValueCallback<Array<Uri>>? = null
    private var fileChooserResultLauncher = createFileChooserResultLauncher()

    private var childView: WebView? = null
    private var transport: WebView.WebViewTransport? = null

    private lateinit var container: FrameLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView

    private var isRedirect = true

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)

        outState.keySet().forEach { key ->
            val bytes = outState.getByteArray(key)
            val sb = Base64.encodeToString(bytes, Base64.DEFAULT)
            sharedVm.saveListKeyInShared(key, sb)
        }

        super.onSaveInstanceState(outState)
    }

    private fun getListHistory() {

        val pref = sharedVm.getSaveLink(HISTORY_KEY)
        val byte = Base64.decode(pref, Base64.DEFAULT)

        val bundle = Bundle()
        bundle.putByteArray(HISTORY_KEY, byte)

        val a = webView.restoreState(bundle)
        val historyCounter = a?.size

        if (historyCounter != null) {
            if (historyCounter > 100) {
                webView.clearHistory()
                webView.clearCache(true)
            }
        }

    }

    private fun url(): String {
        val fullLink = sharedVm.getFullLinkFromShared()
        val saveLink = webView.url

        return if (!saveLink.isNullOrEmpty()) {
            saveLink
        } else if (!fullLink.isNullOrEmpty()) {
            fullLink
        } else
            "https://www.google.com/"
    }

    override fun onCreateView(
        inflater: LayoutInflater, group: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        return createWebView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackPressed()
        hideSystemBar()
        getListHistory()

        initWebView()

    }

    private fun createWebView() = requireContext().let {
        container = FrameLayout(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        progressBar = ProgressBar(it).apply {
            val size = 42.px
            layoutParams = FrameLayout.LayoutParams(size, size).apply {
                gravity = Gravity.CENTER
            }
        }

        progressBar.indeterminateDrawable
            .setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.black),
                PorterDuff.Mode.SRC_IN
            )

        webView = WebView(it).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            visibility = View.GONE
        }

        listOf(webView, progressBar).forEach { v -> container.addView(v) }

        container
    }

    private fun initWebView() {
        webView.apply {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            setWebViewSettings()
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webViewClient = myWebViewClient

            webChromeClient = myWebChromeClient

            loadUrl(this@mnbvcerteyru67687tre5wghe6jr75kERTRYUIudysg.url())

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebView.setWebViewSettings() {
        settings.apply {
            javaScriptCanOpenWindowsAutomatically = true
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            useWideViewPort = true
            allowContentAccess = true
            allowFileAccess = true
            loadWithOverviewMode = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            setSupportMultipleWindows(true)
            setSupportZoom(true)
        }

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.setAcceptThirdPartyCookies(this, true)
        }

        this.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
    }

    private val myWebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest
        ): Boolean {
            val urlLoading = request.url.toString()

            val redirectLinkWV = sharedVm.getRedirectLinkFromShared()
            val isEmptyRedirectLink = !redirectLinkWV.isNullOrEmpty()

            if (request.isRedirect && isEmptyRedirectLink && isRedirect){
                isRedirect = false

                if (urlLoading.contains(redirectLinkWV!!)) {
                    goToNextFragment(MenuGameFragment())
                }
            }

            if (urlLoading.contains("instagram")
                || urlLoading.contains("viber")
                || urlLoading.contains("telegram")
            ) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlLoading))
                startActivity(intent)
            }
            return false
        }

        override fun onPageFinished(layout: WebView?, webURL: String?) {
            super.onPageFinished(layout, webURL)

            webView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private val myWebChromeClient = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            try {
                fileChooserValueCallback = filePathCallback

                fileChooserResultLauncher.launch(fileChooserParams?.createIntent())
            } catch (e: ActivityNotFoundException) {
                // You may handle "No activity found to handle intent" error
            }
            return true
        }

        var fullscreen: View? = null

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            webView.visibility = View.GONE

            if (fullscreen != null) {
                (requireActivity().window.decorView as FrameLayout).removeView(fullscreen)
                requireActivity().window.decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
                fullscreen = null
            }

            fullscreen = view
            (requireActivity().window.decorView as FrameLayout).addView(
                fullscreen,
                FrameLayout.LayoutParams(-1, -1)
            )
            requireActivity().window.decorView
                .setSystemUiVisibility(3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            super.onShowCustomView(view, callback)
        }

        override fun onHideCustomView() {
            fullscreen!!.visibility = View.GONE
            webView.visibility = View.VISIBLE

            (requireActivity().window
                .decorView as FrameLayout).removeView(fullscreen)
            requireActivity().window
                .decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

            fullscreen = null
            super.onHideCustomView()
        }

        @SuppressLint("SetJavaScriptEnabled")
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            childView = WebView(requireContext())
            childView!!.settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }

            childView!!.webChromeClient = this
            childView!!.webViewClient = WebViewClient()

            childView!!.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT
            )
            (requireActivity().window.decorView as FrameLayout).addView(childView)

            transport = resultMsg!!.obj as WebViewTransport
            transport!!.webView = childView
            resultMsg.sendToTarget()
            return true

        }

        override fun onCloseWindow(window: WebView?) {
            super.onCloseWindow(window)
            childView = null
            transport = null
            (requireActivity().window.decorView as FrameLayout).removeView(window)
        }

    }

    private fun createFileChooserResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fileChooserValueCallback?.onReceiveValue(arrayOf(Uri.parse(it?.data?.dataString)));
            } else {
                fileChooserValueCallback?.onReceiveValue(null)
            }
        }
    }

    private fun hideSystemBar() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)

        WindowInsetsControllerCompat(
            requireActivity().window,
            requireActivity().window.decorView.rootView
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            requireActivity().window.decorView.setOnSystemUiVisibilityChangeListener {
                if (it != 0 && View.SYSTEM_UI_FLAG_FULLSCREEN != 0) {
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }

                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

        }
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (childView != null) {
                        (requireActivity().window.decorView as FrameLayout).removeView(transport!!.webView)
                        transport = null
                        childView = null
                    } else {
                        webView.goBack()
                    }
                }
            })
    }

    fun goToNextFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            replace(R.id.containerFragment, fragment)
        }
    }

}
