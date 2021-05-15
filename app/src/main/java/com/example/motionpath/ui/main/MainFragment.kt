package com.example.motionpath.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.motionpath.R

class MainFragment : Fragment() {

    private lateinit var tvTest: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        tvTest = view.findViewById(R.id.tv_test)

        return view
    }
}