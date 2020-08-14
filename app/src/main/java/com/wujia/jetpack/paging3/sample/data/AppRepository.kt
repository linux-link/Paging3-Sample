package com.wujia.jetpack.paging3.sample.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wujia.jetpack.paging3.sample.data.local.RepoDataBase
import com.wujia.jetpack.paging3.sample.data.remote.GithubService
import com.wujia.jetpack.paging3.sample.model.Repo
import kotlinx.coroutines.flow.Flow

const val PAGE_SIZE = 10

class AppRepository(
    private val service: GithubService,
    private val dataBase: RepoDataBase
) {

    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        //TODO : Flow是什么？
        //TODO : PagingData是什么？
        //TODO : Pager是什么？
        //TODO : PagingConfig是什么？
        //TODO : RemoteMediator是什么？
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { dataBase.reposDao().findReposByName(dbQuery) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = GithubRemoteMediator(
                query, service, dataBase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}