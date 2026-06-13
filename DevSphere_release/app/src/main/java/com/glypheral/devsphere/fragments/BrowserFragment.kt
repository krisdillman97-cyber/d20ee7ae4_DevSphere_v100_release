package com.glypheral.devsphere.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.webkit.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.glypheral.devsphere.R
import com.glypheral.devsphere.models.BrowserTab
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager
import com.glypheral.devsphere.utils.toHttpsUrl
import com.glypheral.devsphere.utils.show
import com.glypheral.devsphere.utils.hide

class BrowserFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var urlBar: EditText
    private lateinit var progressBar: ProgressBar
    private val tabs = mutableListOf<BrowserTab>()
    private var devToolsOpen = false

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_browser, c, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = SessionManager.getUser()
        val isDev = user?.role?.canUseDevTools() == true

        webView     = view.findViewById(R.id.webView)
        urlBar      = view.findViewById(R.id.etUrl)
        progressBar = view.findViewById(R.id.progressBar)

        setupWebView(isDev)
        setupUrlBar()
        setupTabBar(view)
        setupDevToolsToggle(view, isDev)

        // Load default
        loadUrl("https://www.google.com")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(isDev: Boolean) {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
            if (isDev) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                urlBar.setText(url)
            }
            override fun shouldOverrideUrlLoading(v: WebView?, req: WebResourceRequest?): Boolean {
                return false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress == 100) progressBar.hide() else progressBar.show()
            }
            override fun onReceivedTitle(view: WebView?, title: String?) {
                // Update tab title
            }
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources) // Grant camera/mic/geo for Developer+
            }
        }
    }

    private fun setupUrlBar() {
        urlBar.setOnEditorActionListener { _, _, _ ->
            val url = urlBar.text.toString().trim().toHttpsUrl()
            loadUrl(url)
            true
        }
    }

    private fun setupTabBar(view: View) {
        view.findViewById<Button>(R.id.btnNewTab)?.setOnClickListener {
            openNewTab()
        }
    }

    private fun setupDevToolsToggle(view: View, isDev: Boolean) {
        val devToolsBtn = view.findViewById<ImageButton>(R.id.btnDevTools)
        val devToolsPanel = view.findViewById<View>(R.id.devToolsPanel)

        if (!isDev) { devToolsBtn?.hide(); return }

        devToolsBtn?.setOnClickListener {
            devToolsOpen = !devToolsOpen
            if (devToolsOpen) {
                devToolsPanel?.show()
                injectDevToolsJS()
            } else {
                devToolsPanel?.hide()
            }
        }
    }

    private fun injectDevToolsJS() {
        // JS Console bridge
        webView.evaluateJavascript("""
            (function() {
                var logs = [];
                var orig = console.log;
                console.log = function() {
                    logs.push(Array.from(arguments).join(' '));
                    orig.apply(console, arguments);
                    Android.onConsoleLog(logs[logs.length-1]);
                };
            })();
        """.trimIndent(), null)
    }

    private fun loadUrl(url: String) {
        webView.loadUrl(url)
        urlBar.setText(url)
    }

    private fun openNewTab() {
        val tab = BrowserTab(title = "New Tab", url = "https://www.google.com")
        tabs.add(tab)
        loadUrl(tab.url)
    }

    override fun onDestroyView() {
        webView.destroy()
        super.onDestroyView()
    }
}
