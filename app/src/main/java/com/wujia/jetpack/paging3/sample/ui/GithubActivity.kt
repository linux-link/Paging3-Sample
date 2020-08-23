package com.wujia.jetpack.paging3.sample.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wujia.jetpack.paging3.sample.AppInjection
import com.wujia.jetpack.paging3.sample.databinding.ActivityGithubBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

const val LAST_SEARCH_QUERY: String = "last_search_query"
const val DEFAULT_QUERY = "Android"

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class GithubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubBinding
    private lateinit var viewModel: GithubViewModel
    private var adapter = GithubAdapter()

    //协程
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initViewModel()
        initList()
        initSearch(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.etRepository.text.trim().toString())
    }

    private fun initContentView() {
        binding = ActivityGithubBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            AppInjection.provideViewModelFactory()
        ).get(GithubViewModel::class.java)
    }

    private fun initList() {
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter
    }

    private fun initSearch(savedInstanceState: Bundle?) {
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        binding.etRepository.setText(query)
        binding.etRepository.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.etRepository.text.trim().let {
                    if (it.isNotEmpty()) {
                        search(it.toString())
                    }
                }
                true
            } else {
                false
            }
        }
        search(query)
    }

    private fun search(query: String) {
        //取消协程任务
        searchJob?.cancel()
        //以lifecycleScope为作用域，开启一个协程
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}