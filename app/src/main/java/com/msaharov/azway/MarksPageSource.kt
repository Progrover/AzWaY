package com.msaharov.azway

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.msaharov.azway.managers.AppwriteManager
import com.msaharov.azway.models.Cursor
import com.msaharov.azway.models.Mark
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MarksPageSource(var bundle: Bundle ?= null) : PagingSource<Cursor, Mark>() {



    override fun getRefreshKey(state: PagingState<Cursor, Mark>): Cursor? {
        return state.anchorPosition?.let {
            val nextKey = state.closestPageToPosition(state.anchorPosition!!)?.nextKey
            val prevKey = state.closestPageToPosition(state.anchorPosition!!)?.prevKey
            if (nextKey != null) {
                return nextKey.lastCursor
            }
            if (prevKey != null) {
                return prevKey.lastCursor
            }
            return null
        }
    }

    override suspend fun load(params: LoadParams<Cursor>): LoadResult<Cursor, Mark> {
        return try {
            var marks: ArrayList<Mark>?
            marks = AppwriteManager.getPagedMarks(params.key, params.loadSize)

            if (marks == null) {
                marks = ArrayList()
            }
            params.key?.lastCursor = null

            val page = params.key?.page ?: 1

            val nextKey: Cursor?
            val prevKey: Cursor?
            if (marks.size < params.loadSize) {
                nextKey = null
            } else {
                nextKey = Cursor(marks.get(marks.size - 1).uuid, Cursor.CursorType.ASC)
                nextKey.page.plus(1)
                nextKey.lastCursor = params.key
            }
            if (page == 1) {
                prevKey = null
            } else {
                prevKey = Cursor(marks.get(0).uuid, Cursor.CursorType.DESC)
                prevKey.page.minus(1)
                prevKey.lastCursor = params.key
            }

            LoadResult.Page(marks, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        } catch (exception: AppwriteException) {
            LoadResult.Error(exception)
        }
    }

    companion object {

        private const val PAGE_SIZE = 25

        @JvmStatic
        fun newFlow(viewModel: ViewModel, bundle: Bundle ?= null): StateFlow<PagingData<Mark>> {
            return Pager(

                config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE),
                pagingSourceFactory = { MarksPageSource(bundle) }

            ).flow
                .cachedIn(viewModel.viewModelScope)
                .stateIn(
                    initialValue = PagingData.empty(),
                    started = SharingStarted.WhileSubscribed(5_000),
                    scope = viewModel.viewModelScope
                )
        }
    }
}