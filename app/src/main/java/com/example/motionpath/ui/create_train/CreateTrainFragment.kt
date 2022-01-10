package com.example.motionpath.ui.create_train

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.ui.MainActivity
import com.example.motionpath.ui.base.BaseFragment
import com.example.motionpath.ui.exercise.adapter.ExerciseAdapter
import com.example.motionpath.util.CalendarManager
import com.example.motionpath.util.DialogHelpers
import com.example.motionpath.util.toStringFormat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CreateTrainFragment : BaseFragment() {

    companion object {
        const val KEY_SCREEN_MODE = "key_screen_mode"
        const val KEY_CLIENT_ID = "key_client_id"
        const val KEY_TRAIN_ID = "key_train_id"
        const val KEY_CURRENT_DATE = "key_current_date"
        const val KEY_TRAIN_END_DATE = "key_train_end_date"

        const val DEFAULT_ID = -1
        const val DEFAULT_TEXT = ""

        fun createTrainArgs(date: Date?) = bundleOf(
            KEY_SCREEN_MODE to CreateTrainViewModel.Mode.CREATE,
            KEY_CURRENT_DATE to date,
            KEY_CLIENT_ID to DEFAULT_ID,
        )

        fun editTrainArgs(clientId: Int?, trainId: Int?, date: Date?, timeEnd: Date?) = bundleOf(
            KEY_SCREEN_MODE to CreateTrainViewModel.Mode.EDIT,
            KEY_CURRENT_DATE to date,
            KEY_CLIENT_ID to clientId,
            KEY_TRAIN_END_DATE to timeEnd,
            KEY_TRAIN_ID to trainId
        )
    }

    @Inject
    lateinit var assistedFactory: CreateTrainViewModelAssistedFactory

    private val viewModel: CreateTrainViewModel by viewModels { Factory(assistedFactory, requireArguments()) }
    private val adapter: ExerciseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ExerciseAdapter(
            requireContext(),
            onItemClick = {

            },
            onItemRemoveClick = {

            }
        )
    }

    private lateinit var viewToolbar: Toolbar
    private lateinit var viewInputDate: TextInputLayout
    private lateinit var viewEditDate: TextInputEditText
    private lateinit var viewInputTime: TextInputLayout
    private lateinit var viewEditTime: TextInputEditText
    private lateinit var viewEditTimeEnd: TextInputEditText
    private lateinit var viewInputName: TextInputLayout
    private lateinit var viewEditName: TextInputEditText
    private lateinit var viewInputDescription: TextInputLayout
    private lateinit var viewEditDescription: TextInputEditText
    private lateinit var viewInputGoal: TextInputLayout
    private lateinit var viewEditGoal: TextInputEditText
    private lateinit var tvSave: TextView
    private lateinit var tvAddExercises: TextView
    private lateinit var rvExrcises: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val bundle = requireArguments()
        when(requireNotNull(bundle[KEY_SCREEN_MODE])) {
            CreateTrainViewModel.Mode.CREATE -> requireNotNull(bundle[KEY_CURRENT_DATE])
            CreateTrainViewModel.Mode.EDIT -> {
                requireNotNull(bundle[KEY_CURRENT_DATE])
                requireNotNull(bundle[KEY_CLIENT_ID])
                requireNotNull(bundle[KEY_TRAIN_ID])
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewToolbar = view.findViewById(R.id.toolbar_create_session)
        viewInputName = view.findViewById(R.id.input_name)
        viewEditName = view.findViewById(R.id.input_edit_name)
        viewInputDescription = view.findViewById(R.id.input_description)
        viewEditDescription = view.findViewById(R.id.input_edit_description)
        viewInputGoal = view.findViewById(R.id.input_goal)
        viewEditGoal = view.findViewById(R.id.input_edit_goal)
        viewInputDate = view.findViewById(R.id.input_date)
        viewEditDate = view.findViewById(R.id.input_edit_date)
        viewInputTime = view.findViewById(R.id.input_time)
        viewEditTime = view.findViewById(R.id.input_edit_time)
        viewEditTimeEnd = view.findViewById(R.id.input_edit_time_end)
        tvSave = view.findViewById(R.id.tv_save)
        tvAddExercises = view.findViewById(R.id.tv_add_activity)
        rvExrcises = view.findViewById(R.id.rv_exercises)

        initToolbar()
        observeData()
        initRecyclerView()

        viewEditDate.setOnClickListener { showDatePickerDialog(viewModel.state.value?.date?.day) }
        viewEditTime.setOnClickListener { showTimePickerDialog(PickedTime.START) }
        viewEditTimeEnd.setOnClickListener { showTimePickerDialog(PickedTime.END) }
        tvSave.setOnClickListener {
            viewModel.applyTrainChanges(
                viewEditName.text.toString(),
                viewEditDescription.text.toString(),
                viewEditGoal.text.toString()
            )
            //navigateBack(requireActivity())
        }
        tvAddExercises.setOnClickListener {
            navigate(requireActivity(), R.id.action_navigation_create_session_to_ExerciseFragment)
        }
    }

    private fun observeData() {
        viewModel.state.observe(viewLifecycleOwner, {
            renderDateState(it)
            renderClientState(it)
            adapter.submitList(it.exercises.map { it.mockExercise })
        })
    }

    private fun initRecyclerView() {
        rvExrcises.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvExrcises.setHasFixedSize(true)
        rvExrcises.adapter = adapter
    }

    private fun showDatePickerDialog(initialDate: Date? = null) {
        DialogHelpers.showDatePickerDialog(
            requireContext(),
            initialDate,
            onDateSelectedCallback = { viewModel.updateDay(it) }
        )
    }

    private fun showTimePickerDialog(pickedTime: PickedTime) {
        val fragment = TimePickerFragment()
        fragment.show(
            (requireActivity() as MainActivity).supportFragmentManager,
            TimePickerFragment::class.java.name
        )
        fragment.apply {
            setOnTimeSelectedCallback(
                onTimeSelected = { hourOfDay, minute ->
                    viewModel.updateTime(hourOfDay, minute, pickedTime)
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

    private fun renderDateState(state: CreateTrainViewModel.State) {
        viewEditDate.setText(
            state.date.day.toStringFormat(CalendarManager.DAY_MONTH_WEEKDAY),
            TextView.BufferType.NORMAL
        )
        viewEditTime.setText(
            state.date.timeStart.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT),
            TextView.BufferType.NORMAL
        )
        viewEditTimeEnd.setText(
            state.date.timeEnd.toStringFormat(CalendarManager.HOUR_MiNUTE_FORMAT),
            TextView.BufferType.NORMAL
        )
    }

    private fun renderClientState(state: CreateTrainViewModel.State) {
        if (state.client.name.isNotEmpty()) {
            viewEditName.setText(state.client.name, TextView.BufferType.NORMAL)
        }
        if (state.client.description.isNotEmpty()) {
            viewEditDescription.setText(state.client.description, TextView.BufferType.NORMAL)
        }
        if (state.client.goal.isNotEmpty()) {
            viewEditGoal.setText(state.client.goal, TextView.BufferType.NORMAL)
        }
    }
}

enum class PickedTime {
    START, END
}