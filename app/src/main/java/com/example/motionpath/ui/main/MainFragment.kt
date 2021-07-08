package com.example.motionpath.ui.main

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
import com.example.motionpath.MainActivity
import com.example.motionpath.R
import com.example.motionpath.data.db.AppDatabase
import com.example.motionpath.data.session.SessionRepositoryImpl
import com.example.motionpath.domain.usecase.CreateSessionUseCase
import com.example.motionpath.domain.usecase.DeleteSessionUseCase
import com.example.motionpath.domain.usecase.GetSessionsUseCase
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.model.domain.Session
import com.example.motionpath.ui.main.adapter.CalendarAdapter
import com.example.motionpath.ui.main.adapter.SessionsAdapter
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.DialogHelpers
import com.example.motionpath.util.toStringFormat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var sessionsAdapter: SessionsAdapter

    private lateinit var rvCalendarDays: RecyclerView
    private lateinit var rvSessions: RecyclerView
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvAddSession: TextView

    private var currentDate: Date? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sessionDao = AppDatabase.getInstance(requireContext()).sessionDao()
        val sessionRepository = SessionRepositoryImpl(sessionDao)
        val getSessionsUseCase = GetSessionsUseCase(sessionRepository)
        val createSessionUseCase = CreateSessionUseCase(sessionRepository)
        val deleteSessionUseCase = DeleteSessionUseCase(sessionRepository)

        viewModel = ViewModelProvider(
                this,
                MainViewModelFactory(getSessionsUseCase, createSessionUseCase, deleteSessionUseCase)
            ).get(MainViewModel::class.java)

        calendarAdapter = CalendarAdapter(
            requireContext(),
            Resources.getSystem().displayMetrics.widthPixels,
            onCalendarDayClick = ::onCalendarDayClick,
        ).apply {
            setSelectedDate(viewModel.today)
        }

        sessionsAdapter = SessionsAdapter(requireContext(), onDeleteSession = ::onSessionLongClick)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        rvCalendarDays = view.findViewById(R.id.rv_calendar_days)
        rvSessions = view.findViewById(R.id.rv_sessions)
        tvSelectedDate = view.findViewById(R.id.tv_selected_date)
        tvAddSession = view.findViewById(R.id.tv_add_session)

        tvAddSession.setOnClickListener {
            (requireActivity() as MainActivity).navController.navigate(R.id.navigation_create_session)
//           DialogHelpers.showDatePickerDialog(
//               requireContext(),
//               currentDate ?: viewModel.today,
//               onDateSelectedCallback = { viewModel.createSessionAt(date = it) })
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

        viewModel.sessions.observe(viewLifecycleOwner, Observer { sessions ->
           sessionsAdapter.submitList(sessions)
            sessionsAdapter.getFirstClientPos().let { pos ->
                if (pos != -1) {
                    rvSessions.scrollToPosition(pos)
                }
            }
        })
    }

    private fun onCalendarDayClick(calendarDay: CalendarDay) {
        viewModel.selectDate(calendarDay.date)
        calendarAdapter.setSelectedDate(calendarDay.date)
        calendarAdapter.notifyDataSetChanged()
    }

    private fun onSessionLongClick(session: Session, anchor: View) {
        DialogHelpers.showPopupMenu(
            requireContext(),
            R.menu.menu_session_options,
            anchor,
            onMenuEditClick = {},
            onMenuNotificationClick = {},
            onMenuDeleteClick = { viewModel.deleteSession(session) },
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

        with(rvSessions) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sessionsAdapter
        }
    }
}