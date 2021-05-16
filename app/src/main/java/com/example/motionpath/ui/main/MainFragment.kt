package com.example.motionpath.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.ui.main.adapter.CalendarAdapter
import com.example.motionpath.util.Logger
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var calendarAdapter: CalendarAdapter

    private lateinit var rvCalendarDays: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        calendarAdapter = CalendarAdapter()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        rvCalendarDays = view.findViewById(R.id.rv_calendar_days)

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
    }

    private fun initViews() {
        rvCalendarDays.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCalendarDays.adapter = calendarAdapter
        rvCalendarDays.hasFixedSize()
    }
}