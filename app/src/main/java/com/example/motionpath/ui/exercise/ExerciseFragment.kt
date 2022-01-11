package com.example.motionpath.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.domain.ExerciseSelectionRepository
import com.example.motionpath.ui.base.BaseFragment
import com.example.motionpath.ui.exercise.adapter.ExerciseAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseFragment : BaseFragment() {

    companion object {
        private const val KEY_TRAIN_ID = "key_train_id"

        fun createSelectExerciseArgs(trainId: Int) = bundleOf(
            KEY_TRAIN_ID to trainId
        )
    }

    @Inject
    lateinit var exerciseSelectionRepository: ExerciseSelectionRepository

    private lateinit var rv: RecyclerView
    private lateinit var toolbar: Toolbar

    private val viewModel: ExerciseViewModel by viewModels()
    private val adapter: ExerciseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ExerciseAdapter(
            requireContext(),
            onItemClick = {
                viewModel.processEvent(ExerciseEvent.onItemClicked(it))
            },
            onItemRemoveClick = {
                viewModel.processEvent(ExerciseEvent.onItemRemoveClicked(it))
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        rv = view.findViewById(R.id.rv)
        toolbar = view.findViewById(R.id.view_toolbar)

        initRecyclerView()
        initToolbar()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.viewStates().collect { renderState(it) }
        }
    }

    private fun renderState(state: ExerciseUiState?) {
        state?.let {
            when (it.status) {
                is Status.Data -> {
                    if (it.depth == SelectionDepth.CATEGORY) {
                        hideToolbarBackButton()
                        adapter.submitList(it.selectedExercises + it.categories)
                    } else {
                        showToolbarBackButton(it.status.category?.getLocalizedName(requireContext()))
                        adapter.submitList(it.exercise)
                    }
                }

                Status.Search -> {
                    adapter.submitList(it.exercise)
                }

                Status.Empty -> {
                    adapter.submitList(emptyList())
                }

                is Status.Error -> Unit

                Status.Loading -> Unit
            }
        }
    }

    private fun initRecyclerView() {
        rv.itemAnimator = null
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.setHasFixedSize(true)
        rv.adapter = adapter
    }

    private fun initToolbar() {
        hideToolbarBackButton()
        toolbar.setNavigationOnClickListener { viewModel.processEvent(ExerciseEvent.onBackButtonClicked)}
    }

    private fun hideToolbarBackButton() {
        toolbar.title = getString(R.string.title_exercise)
        toolbar.navigationIcon = null
    }

    private fun showToolbarBackButton(title: String?) {
        toolbar.title = title
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    }
}