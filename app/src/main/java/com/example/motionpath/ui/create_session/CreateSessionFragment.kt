package com.example.motionpath.ui.create_session

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.motionpath.R
import com.example.motionpath.data.db.AppDatabase
import com.example.motionpath.data.session.SessionRepositoryImpl
import com.example.motionpath.domain.usecase.CreateSessionUseCase
import com.example.motionpath.ui.MainActivity
import com.example.motionpath.ui.base.BaseFragment
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.DialogHelpers
import com.example.motionpath.util.toStringFormat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class CreateSessionFragment : BaseFragment(R.layout.fragment_create_session) {
    private lateinit var viewModel: CreateSessionViewModel

    private lateinit var viewToolbar: Toolbar
    private lateinit var viewInputDate: TextInputLayout
    private lateinit var viewEditDate: TextInputEditText
    private lateinit var viewInputTime: TextInputLayout
    private lateinit var viewEditTime: TextInputEditText
    private lateinit var tvSave: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sessionDao = AppDatabase.getInstance(requireContext()).sessionDao()
        val sessionRepository = SessionRepositoryImpl(sessionDao)
        val createSessionUseCase = CreateSessionUseCase(sessionRepository)
        viewModel =
            ViewModelProvider(
                this,
                CreateSessionViewModelFactory(createSessionUseCase, arguments)
            ).get(
                CreateSessionViewModel::class.java
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewToolbar = view.findViewById(R.id.toolbar_create_session)
        viewInputDate = view.findViewById(R.id.input_date)
        viewEditDate = view.findViewById(R.id.input_edit_date)
        viewInputTime = view.findViewById(R.id.input_time)
        viewEditTime = view.findViewById(R.id.input_edit_time)
        tvSave = view.findViewById(R.id.tv_save)

        initToolbar()
        observeData()

        viewEditDate.setOnClickListener { showDatePickerDialog(viewModel.sessionDate.value) }
        viewEditTime.setOnClickListener { showTimePickerDialog() }
        tvSave.setOnClickListener {
            viewModel.createSession()
            navigateBack(requireActivity())
        }
    }

    private fun observeData() {
        viewModel.sessionDate.observe(viewLifecycleOwner, Observer { initDateView(it) })
    }

    private fun showDatePickerDialog(initialDate: Date? = null) {
        DialogHelpers.showDatePickerDialog(
            requireContext(),
            initialDate,
            onDateSelectedCallback = { viewModel.updateSessionDate(it) }
        )
    }

    private fun showTimePickerDialog() {
        val fragment = TimePickerFragment()
        fragment.show(
            (requireActivity() as MainActivity).supportFragmentManager,
            TimePickerFragment::class.java.name
        )
        fragment.apply {
            setOnTimeSelectedCallback(
                onTimeSelected = { hourOfDay, minute ->
                    viewModel.updateSessionTime(hourOfDay, minute)
                }
            )
        }
    }

    private fun initToolbar() {
        with(viewToolbar) {
            title = getString(R.string.title_create_session)
            navigationIcon =
                ContextCompat.getDrawable(requireContext(), R.drawable.abc_ic_ab_back_material)
            setOnClickListener { navigateBack(requireActivity()) }
        }
    }

    private fun initDateView(date: Date?) {
        date?.let {
            viewEditDate.setText(
                date.toStringFormat(CalendarManager.DAY_MONTH_WEEKDAY),
                TextView.BufferType.NORMAL
            )

            viewEditTime.setText(
                date.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT),
                TextView.BufferType.NORMAL
            )
        }
    }
}