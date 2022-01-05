package com.example.motionpath.ui.base

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.motionpath.ui.MainActivity
import com.example.motionpath.util.Logger
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    protected fun navigate(activity: Activity, @IdRes actionId: Int, bundle: Bundle? = null) {
        (activity as? MainActivity)?.navController?.navigate(actionId, bundle)
            ?: throw IllegalArgumentException("$activity cannot be null, while executing: $actionId")
    }

    protected fun navigateBack(activity: Activity) {
        (activity as? MainActivity)?.navController?.popBackStack()
            ?: throw IllegalArgumentException("$activity cannot be null, while executing navigate back action")
    }

    protected fun showSnackBar(@StringRes textId: Int) {
        Snackbar.make(requireView(), textId, BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    protected fun logError(throwable: Throwable? = null,
                           message: String? = null,
                           @StringRes resId: Int? = null) {
        when {
            throwable != null -> Logger.log(throwable)
            message != null -> Logger.log(message)
            resId != null -> Logger.log(getString(resId))
            else -> Unit
        }
    }
}