package com.example.newsapplication.presenter.screen.webview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapplication.R
import com.example.newsapplication.presenter.screen.ARTICLE_URL
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(intent.getStringExtra(ARTICLE_URL) ?: "")
        webView.webViewClient = WebViewClients(pbLoading)
    }
}