package com.wujia.jetpack.paging3.sample.ui.net

import androidx.paging.PagingSource
import com.wujia.jetpack.paging3.sample.data.remote.GitHubService
import com.wujia.jetpack.paging3.sample.data.remote.IN_QUALIFIER
import com.wujia.jetpack.paging3.sample.model.Repo
import retrofit2.HttpException
import java.io.IOException

const val DEFAULT_INDEX = 1

//TODO: PagingSource是什么？
class NetPagingSource(
    private val service: GitHubService,
    private val query: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        //TODO : LoadParams是什么？
        //TODO : LoadResult是什么？
        val position = params.key ?: DEFAULT_INDEX
        val apiQuery = query + IN_QUALIFIER
        return try {
            val response = service.searchRepos(apiQuery, position, params.loadSize)
            val repos = response.items
            LoadResult.Page(
                data = repos,
                prevKey = if (position == DEFAULT_INDEX) {
                    null
                } else {
                    position - 1
                },
                nextKey = if (repos.isEmpty()) {
                    null
                } else {
                    position + 1
                }
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }

}