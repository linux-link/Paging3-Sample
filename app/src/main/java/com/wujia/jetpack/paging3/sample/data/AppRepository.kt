package com.wujia.jetpack.paging3.sample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wujia.jetpack.paging3.sample.data.remote.GitHubService
import com.wujia.jetpack.paging3.sample.model.Repo
import com.wujia.jetpack.paging3.sample.ui.net.NetPagingSource
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 10

class AppRepository(private val service: GitHubService) {

    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        //TODO : Flow是什么？
        //TODO : PagingData是什么？
        //TODO : Pager是什么？
        //TODO : PagingConfig是什么？
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                NetPagingSource(service = service, query = query)
            }
        ).flow
    }

}