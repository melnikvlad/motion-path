package com.example.motionpath.ui.create_train

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment() : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var onTimeSelected: ((hourOfDay: Int, minute: Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(), this, hour, minute, is24HourFormat(requireContext()))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        onTimeSelected?.invoke(hourOfDay, minute)
    }

    fun setOnTimeSelectedCallback(onTimeSelected: (hourOfDay: Int, minute: Int) -> Unit) {
        this.onTimeSelected = onTimeSelected
    }
}