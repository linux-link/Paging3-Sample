package com.wujia.jetpack.paging3.sample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wujia.jetpack.paging3.sample.model.Repo
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 10

class NetworkRepository(
    private val service: GithubService
) {

    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        val pagingSourceFactory = { GithubPagingSource(service, query) }

        return Pager(
            config = PagingConfig(
                // 每页的item个数。第一页（即首次请求时）pageSize = pageSize * 3 个item
                pageSize = PAGE_SIZE,
                // 是否开启占位
                enablePlaceholders = false,
                // 距离底部有多少个item时，请求下一页
                prefetchDistance = 2
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}