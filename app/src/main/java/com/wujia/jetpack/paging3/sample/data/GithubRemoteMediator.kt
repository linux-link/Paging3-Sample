package com.wujia.jetpack.paging3.sample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.wujia.jetpack.paging3.sample.data.local.RemoteKeys
import com.wujia.jetpack.paging3.sample.data.local.RepoDataBase
import com.wujia.jetpack.paging3.sample.data.remote.GitHubService
import com.wujia.jetpack.paging3.sample.data.remote.IN_QUALIFIER
import com.wujia.jetpack.paging3.sample.model.Repo
import com.wujia.jetpack.paging3.sample.ui.net.DEFAULT_INDEX
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

//TODO: 注解的意思
//TODO: RemoteMediator的左右

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val query: String,
    private val service: GitHubService,
    private val repoDataBase: RepoDataBase
) : RemoteMediator<Int, Repo>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                val prevKey = remoteKeys.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }
        try {
            val apiQuery = query + IN_QUALIFIER
            val apiResponse = service.searchRepos(apiQuery, page, state.config.pageSize)

            val repos = apiResponse.items
            val endOfPaginationReached = repos.isEmpty()
            repoDataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    repoDataBase.remoteKeysDao().clearRemoteKeys()
                    repoDataBase.reposDao().clearRepos()
                }
                val preKeys = if (page == DEFAULT_INDEX) {
                    null
                } else {
                    page - 1
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (ex: IOException) {
            return MediatorResult.Error(ex)
        } catch (ex: HttpException) {
            return MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo: Repo ->
                repoDataBase.remoteKeysDao().findRemoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo: Repo ->
                repoDataBase.remoteKeysDao().findRemoteKeysRepoId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repoDataBase.remoteKeysDao().findRemoteKeysRepoId(repoId)
            }
        }
    }
}