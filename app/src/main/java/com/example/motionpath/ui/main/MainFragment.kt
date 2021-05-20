package com.example.motionpath.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motionpath.R
import com.example.motionpath.data.db.AppDatabase
import com.example.motionpath.ui.main.adapter.CalendarAdapter
import com.example.motionpath.util.Logger
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var calendarAdapter: CalendarAdapter

    private lateinit var rvCalendarDays: RecyclerView
    private lateinit var tvDB: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        calendarAdapter = CalendarAdapter()
        val clietnDao = AppDatabase.getInstance(requireContext()).clientDao()
        viewModel = ViewModelProvider(this, MainViewModelFactory(clietnDao)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        rvCalendarDays = view.findViewById(R.id.rv_calendar_days)
        tvDB = view.findViewById(R.id.tv_test)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        observe()
    }

    override fun onResume() {
        super.onResume()
        viewModel.createClient()
    }

    private fun observe() {

        viewModel.calendarDays.observe(viewLifecycleOwner, Observer { pagingData ->
            calendarAdapter.submitData(lifecycle, pagingData)
        })

        viewModel.clients.observe(viewLifecycleOwner, Observer { clients ->
            tvDB.text = clients.toString()
        })
    }

    private fun initViews() {
        rvCalendarDays.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCalendarDays.adapter = calendarAdapter
        rvCalendarDays.hasFixedSize()
    }
}