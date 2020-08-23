package com.wujia.jetpack.paging3.sample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wujia.jetpack.paging3.sample.data.local.RepoDatabase
import com.wujia.jetpack.paging3.sample.data.remote.GithubService
import com.wujia.jetpack.paging3.sample.model.Repo
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 10

class AppRepository(
    private val service: GithubService,
    private val database: RepoDatabase
) {

    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { database.reposDao().findReposByName(dbQuery) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GithubRemoteMediator(
                query, service, database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}