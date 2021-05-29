package com.example.motionpath.ui.create_session

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.motionpath.R

class CreateSessionFragment : Fragment() {

    companion object {
        fun newInstance() = CreateSessionFragment()
    }

    private lateinit var viewModel: CreateSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_session, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateSessionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}