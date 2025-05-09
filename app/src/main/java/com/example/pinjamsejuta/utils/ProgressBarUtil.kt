package com.example.pinjamsejuta.utils

import android.view.View
import android.widget.ProgressBar

object ProgressBarUtil {

    /**
     * Show the progress bar and hide the provided views.
     */
    fun showLoading(progressBar: ProgressBar, vararg viewsToHide: View) {
        progressBar.visibility = View.VISIBLE
        viewsToHide.forEach { it.visibility = View.GONE }
    }

    /**
     * Hide the progress bar and show the provided views.
     */
    fun hideLoading(progressBar: ProgressBar, vararg viewsToShow: View) {
        progressBar.visibility = View.GONE
        viewsToShow.forEach { it.visibility = View.VISIBLE }
    }
}
