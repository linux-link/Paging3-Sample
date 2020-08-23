package com.wujia.jetpack.paging3.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.wujia.jetpack.paging3.sample.data.NetworkRepository
import com.wujia.jetpack.paging3.sample.model.Repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
class GithubViewModel(private val repository: NetworkRepository) : ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<UiModel>>? = null

    fun searchRepo(queryString: String): Flow<PagingData<UiModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<UiModel>> = repository
            .getSearchResultStream(queryString)
            //将原始数据的repo使用map转换成UiModel备用
            .map {
                it.map { repo ->
                    UiModel.RepoItem(repo)
                }
            }
            .map {
                it.insertSeparators<UiModel.RepoItem, UiModel> { before, after ->
                    if (after == null) {
                        return@insertSeparators null
                    }
                    if (before == null) {
                        return@insertSeparators UiModel.SeparatorItem(
                            "${after.roundedStarCount}0.000 + stars"
                        )
                    }
                    if (before.roundedStarCount > after.roundedStarCount) {
                        if (after.roundedStarCount >= 1) {
                            UiModel.SeparatorItem("${after.roundedStarCount}0.000 + stars")
                        } else {
                            UiModel.SeparatorItem("<10.000 + stars")
                        }
                    } else {
                        null
                    }
                }
            }.cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}

sealed class UiModel {
    data class RepoItem(val repo: Repo) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}

private val UiModel.RepoItem.roundedStarCount: Int
    get() = this.repo.stars / 10_000