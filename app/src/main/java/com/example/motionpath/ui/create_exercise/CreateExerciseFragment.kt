package com.example.motionpath.ui.create_exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.motionpath.R
import com.example.motionpath.ui.base.BaseFragment

class CreateExerciseFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_exercise, container)
    }
}