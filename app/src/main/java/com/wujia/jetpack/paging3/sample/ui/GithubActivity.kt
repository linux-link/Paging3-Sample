package com.wujia.jetpack.paging3.sample.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wujia.jetpack.paging3.sample.AppInjection
import com.wujia.jetpack.paging3.sample.databinding.ActivityGithubBinding
import com.wujia.jetpack.paging3.sample.ui.footer.FooterAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val LAST_SEARCH_QUERY: String = "last_search_query"
const val DEFAULT_QUERY = "Android"

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class GithubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubBinding
    private lateinit var viewModel: GithubViewModel
    private var adapter =
        GithubAdapter()

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
        //同时给adapter添加头部和尾部
        binding.list.adapter = adapter.withLoadStateFooter(
            footer = FooterAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener {
            binding.list.isVisible = it.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = it.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = it.source.refresh is LoadState.Error

            //处理错误状态
            val errorState = it.source.append as? LoadState.Error
                ?: it.source.prepend as? LoadState.Error
                ?: it.source.append as? LoadState.Error
                ?: it.append as? LoadState.Error
                ?: it.prepend as? LoadState.Error

            errorState?.let { loadState ->
                Toast.makeText(this, "${loadState.error}", Toast.LENGTH_LONG).show()
            }
        }

        binding.retryButton.setOnClickListener {
            adapter.retry()
        }

    }

    private fun initSearch(savedInstanceState: Bundle?) {
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        binding.etRepository.setText(query)
        binding.etRepository.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
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
        //
        search(query)
    }

    private fun search(query: String) {
        //取消上一次的协程
        searchJob?.cancel()
        //以lifecycleScope为作用域，启动一个协程
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}