package com.example.motionpath.util.extension

import android.view.View
import android.widget.TextView

fun View.visible() {
    if (!isVisible()) {
        visibility = View.VISIBLE
    }
}

fun View.gone() {
    if (!isGone()) {
        visibility = View.GONE
    }
}

fun View.hide() {
    if (!isHidden()) {
        visibility = View.INVISIBLE
    }
}

fun View.setVisible(condition: Boolean) {
    when {
        condition -> visible()
        else -> gone()
    }
}

fun View.setVisibleOrHide(condition: Boolean) {
    when {
        condition -> visible()
        else -> hide()
    }
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isGone() = visibility == View.GONE

fun View.isHidden() = visibility == View.INVISIBLE

fun TextView.visible() {
    if (!isVisible()) {
        visibility = View.VISIBLE
    }
}

fun TextView.gone() {
    if (!isGone()) {
        visibility = View.GONE
    }
}