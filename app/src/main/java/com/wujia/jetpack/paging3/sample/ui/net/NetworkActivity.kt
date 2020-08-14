package com.wujia.jetpack.paging3.sample.ui.net

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wujia.jetpack.paging3.sample.component.AppInjection
import com.wujia.jetpack.paging3.sample.databinding.ActivityNetworkBinding
import com.wujia.jetpack.paging3.sample.ui.net.footer.NetLoadStateAdapter
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
class NetworkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkBinding
    private lateinit var viewModel: NetworkViewModel
    private var adapter = NetworkAdapter()

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
        binding = ActivityNetworkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            AppInjection.provideViewModelFactory(this)
        ).get(NetworkViewModel::class.java)
    }

    private fun initList() {
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
        binding.list.layoutManager = LinearLayoutManager(this)

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = NetLoadStateAdapter {
                adapter.retry()
            },
            footer = NetLoadStateAdapter {
                adapter.retry()
            }
        )

        adapter.addLoadStateListener { loadState ->
            binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.retryButton.setOnClickListener { adapter.retry() }
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
        //TODO : lifecycleScope是什么
        //TODO : launch是什么
        lifecycleScope.launch {
            adapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
        search(query)
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

}