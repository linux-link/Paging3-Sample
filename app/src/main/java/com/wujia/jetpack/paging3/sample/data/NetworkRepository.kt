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
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false,prefetchDistance = 1),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}