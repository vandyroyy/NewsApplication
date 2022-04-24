package com.example.newsapplication.presenter.screen.source

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
import com.example.newsapplication.data.response.BaseSource
import com.example.newsapplication.data.response.Source
import com.example.newsapplication.domain.Interactors
import com.example.newsapplication.presenter.adapter.SourcesAdapter
import com.example.newsapplication.presenter.screen.article.ArticleActivity
import com.example.newsapplication.presenter.screen.CATEGORY_ID
import com.example.newsapplication.presenter.screen.SOURCE_ID
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_sources.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

class SourcesActivity : AppCompatActivity(), SourcesAdapter.OnItemClickListener {

    // state
    var searchedSource: String? = null
    var sources: ArrayList<Source?> = arrayListOf()
    var page = 1
    var category: String? = ""
    lateinit var screenState: ScreenState

    // presenter
    lateinit var adapter: SourcesAdapter

    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)
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
                searchedSource = text
                if (searchedSource?.isNotEmpty() ?: false) {
                    val searchedSources = sources.filter {
                        it?.name?.contains(searchedSource.orEmpty(), true) ?: false ||
                                it?.description?.contains(searchedSource.orEmpty(), true) ?: false
                    }
                    if (searchedSources.isEmpty()) {
                        showEmptyState(this@SourcesActivity.getString(R.string.source_search_result_empty))
                    } else {
                        hideEmptyState()
                    }
                    adapter.setSources(searchedSources)
                } else {
                    adapter.setSources(sources)
                    hideEmptyState()
                }
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                searchedSource = text
                if (searchedSource?.isBlank() ?: false) {
                    this.onQueryTextSubmit(searchedSource)
                }
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun init() {
        category = intent.getStringExtra(CATEGORY_ID)
        linearLayoutManager = LinearLayoutManager(this)
        rvSources.also {
            it.layoutManager = linearLayoutManager
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val visibleItemCount = linearLayoutManager.childCount
                        val pastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
                        val total = adapter.itemCount

                        if (screenState != ScreenState.LOADING || screenState == ScreenState.ERROR) {
                            if ((visibleItemCount + pastVisibleItem) >= total) {
                                loadMoreSources()
                            }
                        }
                    }

                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        adapter = SourcesAdapter(this)
        rvSources.adapter = adapter
        fetchSources()
    }

    override fun onClickSourceItem(source: Source) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(SOURCE_ID, source.id)
        startActivity(intent)
    }

    private fun fetchSources() {
        screenState = ScreenState.LOADING
        pbLoading.visibility = View.VISIBLE
        Interactors.getSources(category).enqueue(object : Callback<BaseSource>{
            override fun onResponse(call: Call<BaseSource>, response: Response<BaseSource>) {
                screenState = ScreenState.SUCCESS
                pbLoading.visibility = View.GONE
                Log.d(TAG, response.raw().toString())
                response.body()?.let {
                    if (it.sources.isNotEmpty()) {
                        sources.addAll(it.sources)
                        adapter.setSources(sources)
                    } else {
                        showEmptyState(this@SourcesActivity.getString(R.string.source_result_empty))
                    }
                } ?: run {
                    showEmptyState(response.message())
                }
            }

            override fun onFailure(call: Call<BaseSource>, t: Throwable) {
                screenState = ScreenState.ERROR
                pbLoading.visibility = View.GONE
                Log.d(TAG, t.toString())
                rvSources.visibility = View.GONE
                if (t is UnknownHostException) {
                    showEmptyState(this@SourcesActivity.getString(R.string.error_general))
                } else {
                    showEmptyState(t.toString())
                }
            }
        })
    }

    private fun loadMoreSources() {
        adapter.addSources(listOf(null))
        screenState = ScreenState.LOADING
        Interactors.getSources(category).enqueue(object : Callback<BaseSource>{
            override fun onResponse(call: Call<BaseSource>, response: Response<BaseSource>) {
                page++
                screenState = ScreenState.SUCCESS
                adapter.removeLoadingItem()
                Log.d(TAG, response.body().toString())
                response.body()?.let {
                    sources.addAll(it.sources)
                    adapter.addSources(sources)
                }
            }

            override fun onFailure(call: Call<BaseSource>, t: Throwable) {
                screenState = ScreenState.ERROR
                adapter.removeLoadingItem()
                Log.d(TAG, t.toString())
                if (t is UnknownHostException) {
                    Snackbar.make(flContent, this@SourcesActivity.getString(R.string.error_general), Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(flContent, t.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showEmptyState(message: String) {
        llEmptyState.visibility = View.VISIBLE
        tvError.text = message
    }

    private fun hideEmptyState() {
        llEmptyState.visibility = View.GONE
    }

    companion object {
        const val TAG = "SOURCE_ACTIVITY"
        enum class ScreenState {
            LOADING, SUCCESS, ERROR
        }
    }
}