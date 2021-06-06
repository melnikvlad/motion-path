package com.example.motionpath.ui.create_session

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.motionpath.MainActivity
import com.example.motionpath.R
import com.example.motionpath.data.db.AppDatabase
import com.example.motionpath.data.session.SessionRepositoryImpl
import com.example.motionpath.domain.usecase.CreateSessionUseCase
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.DialogHelpers
import com.example.motionpath.util.toStringFormat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class CreateSessionFragment : Fragment() {
    private lateinit var viewModel: CreateSessionViewModel

    private lateinit var viewToolbar: Toolbar
    private lateinit var viewInputDate: TextInputLayout
    private lateinit var viewEditDate: TextInputEditText
    private lateinit var tvSave: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sessionDao = AppDatabase.getInstance(requireContext()).sessionDao()
        val sessionRepository = SessionRepositoryImpl(sessionDao)
        val createSessionUseCase = CreateSessionUseCase(sessionRepository)
        viewModel = ViewModelProvider(this, CreateSessionViewModelFactory(createSessionUseCase)).get(CreateSessionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_create_session, container, false)

        viewToolbar = view.findViewById(R.id.toolbar_create_session)
        viewInputDate = view.findViewById(R.id.input_date)
        viewEditDate = view.findViewById(R.id.input_edit_date)
        tvSave = view.findViewById(R.id.tv_save)

        initToolbar()

        viewEditDate.setOnClickListener { showDatePicker() }
        tvSave.setOnClickListener { createSession() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initToolbar() {
        with(viewToolbar) {
            title = getString(R.string.title_create_session)
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.abc_ic_ab_back_material)
            setOnClickListener { close() }
        }
    }

    private fun showDatePicker(initialDate: Date? = null) {
        DialogHelpers.showDatePickerDialog(
            requireContext(),
            initialDate,
            onDateSelectedCallback = { date ->
                viewEditDate.setText(
                    date.toStringFormat(CalendarManager.DEFAULT_FORMAT),
                    TextView.BufferType.NORMAL)
            }
        )
    }

    private fun createSession() {
        viewModel.createSessionAt()
    }

    private fun close() {
        (requireActivity() as MainActivity).navController.popBackStack()
    }
}