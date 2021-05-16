package com.example.motionpath.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.motionpath.model.CalendarDay
import kotlinx.coroutines.flow.Flow
import java.util.*

class CalendarRepository {

    companion object {
        const val PAGE_SIZE = 30
    }

    fun getCalendarDays(startDate: Date) : Flow<PagingData<CalendarDay>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE / 3, enablePlaceholders = false),
            pagingSourceFactory = { CalendarPagingSource(startDate) }
        ).flow
    }
}