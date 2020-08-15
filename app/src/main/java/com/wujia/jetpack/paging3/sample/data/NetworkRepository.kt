package com.wujia.jetpack.paging3.sample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wujia.jetpack.paging3.sample.model.Repo
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 50

class NetworkRepository(
    private val service: GithubService
) {

    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        //TODO : Flow是什么？
        //TODO : PagingData是什么？
        //TODO : Pager是什么？
        //TODO : PagingConfig是什么？
        val pagingSourceFactory = {
            NetPagingSource(
                service,
                query
            )
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}