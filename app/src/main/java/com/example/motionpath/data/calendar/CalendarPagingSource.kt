package com.example.motionpath.data.calendar

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.motionpath.data.calendar.CalendarRepository.Companion.PAGE_SIZE
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.util.CalendarManager
import java.util.*

class CalendarPagingSource(private val startingDate: Date) : PagingSource<Date, CalendarDay>() {

    private var prevKey: Date? = null
    private var nextKey: Date? = null

    override suspend fun load(params: LoadParams<Date>): LoadResult<Date, CalendarDay> {
        val key = params.key ?: startingDate

        val daysBefore = CalendarManager.getDaysBefore(key, PAGE_SIZE)
        val daysAfter = CalendarManager.getDaysAfter(key, PAGE_SIZE)

        prevKey = daysBefore.last().date
        nextKey = daysAfter.last().date

        return LoadResult.Page(
            data = daysAfter,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Date, CalendarDay>): Date? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}