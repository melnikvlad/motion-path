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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.ui.MainActivity
import com.example.motionpath.ui.base.BaseFragment
import com.example.motionpath.ui.create_train.adapter.TrainInfoAdapter
import com.example.motionpath.util.DialogHelpers
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
        const val KEY_TRAIN_START_DATE = "key_train_start_date"
        const val KEY_TRAIN_END_DATE = "key_train_end_date"

        const val DEFAULT_ID = -1
        const val DEFAULT_TEXT = ""

        fun createTrainArgs(date: Date?) = bundleOf(
            KEY_SCREEN_MODE to Mode.CREATE,
            KEY_CURRENT_DATE to date,
            KEY_CLIENT_ID to DEFAULT_ID,
        )

        fun editTrainArgs(
            clientId: Int?,
            trainId: Int?,
            date: Date?,
            timeStart: Date?,
            timeEnd: Date?
        ) = bundleOf(
            KEY_SCREEN_MODE to Mode.EDIT,
            KEY_CURRENT_DATE to date,
            KEY_CLIENT_ID to clientId,
            KEY_TRAIN_START_DATE to timeStart,
            KEY_TRAIN_END_DATE to timeEnd,
            KEY_TRAIN_ID to trainId
        )
    }

    @Inject
    lateinit var assistedFactory: CreateTrainViewModelAssistedFactory

    private val viewModel: CreateTrainViewModel by viewModels {
        Factory(
            assistedFactory,
            requireArguments()
        )
    }
    private val adapter: TrainInfoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TrainInfoAdapter(
            requireContext(),
            onSelectExerciseClick = { viewModel.processEvent(CreateTrainEvent.onAddExerciseClicked) },
            onItemRemoveClick = { viewModel.processEvent(CreateTrainEvent.onRemoveExerciseClicked(it)) },
            onCollapseOrExpandClick = { viewModel.processEvent(CreateTrainEvent.onCollapseOrExpandClicked) },
            onNameChanged = { viewModel.processEvent(CreateTrainEvent.onNameChanged(it)) },
            onSearchClientResultClick = { viewModel.processEvent(CreateTrainEvent.onSearchClientResultClicked(it)) },
            onDateClicked = { viewModel.processEvent(CreateTrainEvent.onDateClicked(it)) },
            onTimeStartClicked = { viewModel.processEvent(CreateTrainEvent.onTimeClicked(PickedTime.START)) },
            onTimeEndClicked = { viewModel.processEvent(CreateTrainEvent.onTimeClicked(PickedTime.END)) },
            onPreviousTrainClicked = { viewModel.processEvent(CreateTrainEvent.onPreviousTrainClicked(it)) }
        )
    }

    private lateinit var viewToolbar: Toolbar
    private lateinit var tvSave: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val bundle = requireArguments()
        when (requireNotNull(bundle[KEY_SCREEN_MODE])) {
            Mode.CREATE -> requireNotNull(bundle[KEY_CURRENT_DATE])
            Mode.EDIT -> {
                requireNotNull(bundle[KEY_CURRENT_DATE])
                requireNotNull(bundle[KEY_CLIENT_ID])
                requireNotNull(bundle[KEY_TRAIN_ID])
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
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

        tvSave = view.findViewById(R.id.tv_save)
        recyclerView = view.findViewById(R.id.rv_exercises)

        initToolbar()
        initRecyclerView()

        tvSave.setOnClickListener {
            viewModel.processEvent(CreateTrainEvent.onSaveClicked)
        }
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewStates()
                .collect { state ->
                    state?.let {
                        adapter.submitList(it.items())
                    }
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.viewActions().collect { action ->
                action?.let {
                    when (it) {
                        CreateTrainAction.NavigateToSelectExerciseAction -> {
                            navigate(
                                requireActivity(),
                                R.id.action_navigation_create_session_to_ExerciseFragment
                            )
                        }

                        CreateTrainAction.NavigateToTrainsAction -> {
                            navigateBack(requireActivity())
                        }

                        is CreateTrainAction.OpenDatePickerAction -> {
                            showDatePickerDialog(it.day)
                        }

                        is CreateTrainAction.OpenTimeStartPickerAction -> {
                            showTimePickerDialog(PickedTime.START)
                        }

                        is CreateTrainAction.OpenTimeEndPickerAction -> {
                            showTimePickerDialog(PickedTime.END)
                        }

                        CreateTrainAction.ScrollToStart -> {
                            recyclerView.smoothScrollToPosition(0)
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView.itemAnimator = null
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun showDatePickerDialog(initialDate: Date? = null) {
        DialogHelpers.showDatePickerDialog(
            requireContext(),
            initialDate,
            onDateSelectedCallback = { viewModel.processEvent(CreateTrainEvent.onDateUpdated(it)) }
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
                    viewModel.processEvent(CreateTrainEvent.onTimeUpdated(hourOfDay, minute, pickedTime))
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
}

enum class PickedTime {
    START, END
}