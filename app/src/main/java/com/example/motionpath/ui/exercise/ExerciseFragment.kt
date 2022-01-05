package com.example.motionpath.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.ui.base.BaseFragment
import com.example.motionpath.ui.exercise.adapter.ExerciseAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ExerciseFragment : BaseFragment() {

    companion object {
        private const val KEY_TRAIN_ID = "key_train_id"

        fun createSelectExerciseArgs(trainId: Int) = bundleOf(
            KEY_TRAIN_ID to trainId
        )
    }

    private lateinit var rv: RecyclerView

    private val viewModel: ExerciseViewModel by viewModels()
    private val adapter: ExerciseAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ExerciseAdapter(
            requireContext(),
            onItemClick = {
                viewModel.processEvent(ExerciseEvent.onItemClicked(it))
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

        initRecyclerView()

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
                Status.Data -> {
                    if (it.depth == SelectionDepth.CATEGORY) {
                        adapter.submitList(it.categories)
                    } else {
                        adapter.submitList(it.exercise)
                    }
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
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv.setHasFixedSize(true)
        rv.adapter = adapter
    }
}