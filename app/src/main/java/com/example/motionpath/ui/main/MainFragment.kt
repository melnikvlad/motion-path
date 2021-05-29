package com.example.motionpath.ui.main

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.data.db.AppDatabase
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.ui.main.adapter.CalendarAdapter
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.toStringFormat
import java.util.*


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var calendarAdapter: CalendarAdapter

    private lateinit var rvCalendarDays: RecyclerView
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvAddSession: TextView

    private lateinit var tvTest: TextView

    private var currentDate: Date? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val clietnDao = AppDatabase.getInstance(requireContext()).clientDao()
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(clietnDao)).get(MainViewModel::class.java)
        calendarAdapter = CalendarAdapter(
            requireContext(),
            Resources.getSystem().displayMetrics.widthPixels,
            onCalendarDayClick = ::onCalendarDayClick
        ).apply {
            setSelectedDate(viewModel.today)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        rvCalendarDays = view.findViewById(R.id.rv_calendar_days)
        tvSelectedDate = view.findViewById(R.id.tv_selected_date)
        tvAddSession = view.findViewById(R.id.tv_add_session)

        tvTest = view.findViewById(R.id.tv_test)

        tvAddSession.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.time = currentDate
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]

            DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonthOfYear, selectedDayOfMonth ->
                    cal.set(Calendar.YEAR, selectedYear)
                    cal.set(Calendar.MONTH, selectedMonthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
                    viewModel.createSession(cal.time)
                },
                year, month, day
            ).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        observe()
    }

    private fun observe() {

        viewModel.calendarDays.observe(viewLifecycleOwner, Observer { pagingData ->
            calendarAdapter.submitData(lifecycle, pagingData)
        })

        viewModel.selectedDate.observe(viewLifecycleOwner, Observer { selectedDate ->
            currentDate = selectedDate
            tvSelectedDate.text = selectedDate.toStringFormat(CalendarManager.MONTH_YEAR_FORMAT)
        })

        viewModel.clients.observe(viewLifecycleOwner, Observer { clients ->
            val sb = StringBuilder()
            for (client in clients) {
                sb.append(client.calendarDay.toStringFormat())
                    .append('\n')
            }

            tvTest.text = sb.toString()
        })
    }

    private fun onCalendarDayClick(calendarDay: CalendarDay) {
        viewModel.selectDate(calendarDay.date)
        calendarAdapter.setSelectedDate(calendarDay.date)
        calendarAdapter.notifyDataSetChanged()
    }

    private fun initViews() {
        with(rvCalendarDays) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = calendarAdapter
            hasFixedSize()
        }
    }
}