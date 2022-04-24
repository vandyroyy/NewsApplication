package com.example.newsapplication.presenter.screen.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.R
import com.example.newsapplication.data.response.Article
import com.example.newsapplication.data.response.BaseArticles
import com.example.newsapplication.domain.Interactors
import com.example.newsapplication.presenter.adapter.ArticlesAdapter
import com.example.newsapplication.presenter.screen.ARTICLE_URL
import com.example.newsapplication.presenter.screen.SOURCE_ID
import com.example.newsapplication.presenter.screen.webview.WebViewActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_article.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class ArticleActivity : AppCompatActivity(), ArticlesAdapter.OnItemClickListener {

    // state
    var source: String? = null
    lateinit var screenState: ScreenState
    var page = 1
    var canLoadMore = true
    var limit = 5
    var searchedArticle: String? = null

    // presentation
    lateinit var adapter: ArticlesAdapter

    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.aBSearch)
        val searchView: SearchView = searchViewItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                searchView.clearFocus()
                searchedArticle = text
                resetQuery()
                fetchArticles()
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                searchedArticle = text
                if (searchedArticle?.isBlank() ?: false) {
                    this.onQueryTextSubmit(searchedArticle)
                }
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onClickArticleItem(article: Article) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(ARTICLE_URL, article.url)
        startActivity(intent)
    }

    private fun init() {
        source = intent.getStringExtra(SOURCE_ID)
        linearLayoutManager = LinearLayoutManager(this)
        rvArticles.also {
            it.layoutManager = linearLayoutManager
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val visibleItemCount = linearLayoutManager.childCount
                        val pastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
                        val total = adapter.itemCount
                        if (canLoadMore && (screenState != ScreenState.LOADING || screenState == ScreenState.ERROR)) {
                            if ((visibleItemCount + pastVisibleItem) >= total) {
                                loadMoreArticle()
                            }
                        }
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        adapter = ArticlesAdapter(this)
        rvArticles.adapter = adapter
        fetchArticles()
    }

    private fun resetQuery() {
        page = 1
        canLoadMore = true
        adapter.setArticles(listOf())
    }

    private fun fetchArticles() {
        screenState = ScreenState.LOADING
        showLoading()
        Interactors.getTopHeadline(
            source = source ?: "",
            page = page,
            limit = limit,
            search = searchedArticle
        ).enqueue(object : Callback<BaseArticles>{
            override fun onResponse(call: Call<BaseArticles>, response: Response<BaseArticles>) {
                screenState = ScreenState.SUCCESS
                Log.d(TAG, response.toString())
                response.body()?.let {
                    if (it.articles.isNotEmpty()) {
                        page++
                        adapter.addArticles(it.articles)
                        canLoadMore = it.articles.size >= limit
                        hideEmptyState()
                    } else {
                        showEmptyState(this@ArticleActivity.getString(R.string.article_result_empty).takeIf {
                            searchedArticle?.isBlank() ?: true
                        } ?: this@ArticleActivity.getString(R.string.article_search_result_empty))
                    }
                    hideLoading()
                } ?: run {
                    showEmptyState(response.message())
                }
            }

            override fun onFailure(call: Call<BaseArticles>, t: Throwable) {
                screenState = ScreenState.ERROR
                hideLoading()
                Log.d(TAG, t.toString())
                if (t is UnknownHostException) {
                    showEmptyState(this@ArticleActivity.getString(R.string.error_general))
                } else {
                    showEmptyState(t.toString())
                }
            }
        })
    }

    private fun loadMoreArticle() {
        adapter.addArticles(listOf(null))
        screenState = ScreenState.LOADING
        Interactors.getTopHeadline(
            source = source ?: "",
            page = page,
            limit = 5,
            search = searchedArticle
        ).enqueue(object : Callback<BaseArticles>{
            override fun onResponse(call: Call<BaseArticles>, response: Response<BaseArticles>) {
                Log.d(TAG, response.toString())
                Log.d(TAG, response.body().toString())
                screenState = ScreenState.SUCCESS
                adapter.removeLoadingItem()
                response.body()?.let {
                    page++
                    adapter.addArticles(it.articles)
                    canLoadMore = it.articles.size >= limit
                }
            }

            override fun onFailure(call: Call<BaseArticles>, t: Throwable) {
                screenState = ScreenState.ERROR
                adapter.removeLoadingItem()
                Log.d(TAG, t.toString())
                if (t is UnknownHostException) {
                    Snackbar.make(flContent, this@ArticleActivity.getString(R.string.error_general), Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(flContent, t.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showEmptyState(message: String) {
        llEmptyState.visibility = View.VISIBLE
        tvError.text = message
        rvArticles.visibility = View.GONE
    }

    private fun hideEmptyState() {
        llEmptyState.visibility = View.GONE
        rvArticles.visibility = View.VISIBLE
    }

    private fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        pbLoading.visibility = View.GONE
    }

    companion object {
        const val TAG = "ARTICLE_ACTIVITY"
        enum class ScreenState {
            LOADING, SUCCESS, ERROR
        }
    }
}