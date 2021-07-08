package com.example.motionpath.util

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import com.example.motionpath.R
import java.util.*

class DialogHelpers {
    companion object {
        fun showDatePickerDialog(context: Context, initDate: Date?, onDateSelectedCallback: (Date) -> Unit) {
            val cal = initDate?.let {
                Calendar.getInstance().apply {
                    time = initDate
                }
            } ?: GregorianCalendar()

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonthOfYear, selectedDayOfMonth ->
                    cal.set(Calendar.YEAR, selectedYear)
                    cal.set(Calendar.MONTH, selectedMonthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
                    onDateSelectedCallback.invoke(cal.time)
                },
                year, month, day
            ).show()
        }

        fun showPopupMenu(context: Context,
                          @MenuRes menuRes: Int,
                          anchorView: View,
                          onMenuEditClick: () -> Unit,
                          onMenuNotificationClick: () -> Unit,
                          onMenuDeleteClick: () -> Unit,
                          onMenuChangeDateClick: () -> Unit
        ) {
            PopupMenu(context, anchorView).apply {
                menuInflater.inflate(menuRes, menu)
                setOnMenuItemClickListener { menuItem ->
                    return@setOnMenuItemClickListener when(menuItem.itemId) {
                        R.id.session_edit -> {
                            onMenuEditClick.invoke()
                            true
                        }
                        R.id.session_notification -> {
                            onMenuNotificationClick.invoke()
                            true
                        }
                        R.id.session_delete -> {
                            onMenuDeleteClick.invoke()
                            true
                        }
                        R.id.session_change_date -> {
                            onMenuChangeDateClick.invoke()
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }
}