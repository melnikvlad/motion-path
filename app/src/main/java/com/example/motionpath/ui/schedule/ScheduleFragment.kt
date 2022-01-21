package com.example.motionpath.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.data.model.entity.relations.TrainWithClient
import com.example.motionpath.ui.base.BaseFragment
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.createTrainArgs
import com.example.motionpath.ui.create_train.CreateTrainFragment.Companion.editTrainArgs
import com.example.motionpath.ui.schedule.adapter.CalendarAdapter
import com.example.motionpath.ui.schedule.adapter.TrainsAdapter
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.DialogHelpers
import com.example.motionpath.util.toStringFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleFragment : BaseFragment() {

    private val viewModel: ScheduleViewModel by viewModels()
    private val calendarAdapter: CalendarAdapter by lazy {
        CalendarAdapter(
            requireContext(),
            onCalendarDayClick = ::onCalendarDayClick,
        ).apply {
            setSelectedDate(viewModel.today) //TODO: get today from Calendar manager
        }
    }
    private val trainAdapter: TrainsAdapter by lazy { TrainsAdapter(::onTrainClick, ::onDeleteTrain) }

    private lateinit var rvCalendarDays: RecyclerView
    private lateinit var rvTrains: RecyclerView
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvAddSession: TextView

    private var currentDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCalendarDays = view.findViewById(R.id.rv_calendar_days)
        rvTrains = view.findViewById(R.id.rv_sessions)
        tvSelectedDate = view.findViewById(R.id.tv_selected_date)
        tvAddSession = view.findViewById(R.id.tv_add_session)

        tvAddSession.setOnClickListener {
            navigate(
                requireActivity(),
                R.id.action_navigation_main_to_navigation_create_session,
                createTrainArgs(currentDate)
            )
        }

        initViews()
        observe()
    }

    private fun observe() {

        viewModel.calendarDays.observe(viewLifecycleOwner, { pagingData ->
            calendarAdapter.submitData(lifecycle, pagingData)
        })

        viewModel.selectedDate.observe(viewLifecycleOwner, { selectedDate ->
            currentDate = selectedDate
            tvSelectedDate.text = selectedDate.toStringFormat(CalendarManager.MONTH_YEAR_FORMAT)
        })

        viewModel.trains.observe(viewLifecycleOwner, { trainAdapter.submitList(it) })
    }

    private fun onTrainClick(trainWithClient: TrainWithClient) {
        navigate(
            requireActivity(),
            R.id.action_navigation_main_to_navigation_create_session,
            editTrainArgs(
                trainWithClient.client.id,
                trainWithClient.train.id,
                currentDate,
                trainWithClient.train.timeStart,
                trainWithClient.train.timeEnd
            )
        )
    }

    private fun onDeleteTrain(trainWithClient: TrainWithClient) {
        viewModel.deleteTrain(trainWithClient.train)
    }

    private fun onCalendarDayClick(calendarDay: CalendarDay) {
        viewModel.selectDate(calendarDay.date)
        calendarAdapter.setSelectedDate(calendarDay.date)
        calendarAdapter.notifyDataSetChanged()
    }

    private fun onSessionLongClick(anchor: View) {
        DialogHelpers.showPopupMenu(
            requireContext(),
            R.menu.menu_session_options,
            anchor,
            onMenuEditClick = {},
            onMenuNotificationClick = {},
            onMenuDeleteClick = {},
            onMenuChangeDateClick = {}
        )
    }

    private fun initViews() {
        with(rvCalendarDays) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = calendarAdapter
            hasFixedSize()
        }

        with(rvTrains) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = trainAdapter
        }
    }
}