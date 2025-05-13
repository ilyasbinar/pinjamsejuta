package com.example.pinjamsejuta.utils

import android.view.View
import com.airbnb.lottie.LottieAnimationView

object ProgressUtil {

    /**
     * Show the Lottie animation and hide the provided views.
     */
    fun showLoading(lottieView: LottieAnimationView, vararg viewsToHide: View) {
        lottieView.visibility = View.VISIBLE
        lottieView.playAnimation()
        viewsToHide.forEach { it.visibility = View.GONE }
    }

    /**
     * Hide the Lottie animation and show the provided views.
     */
    fun hideLoading(lottieView: LottieAnimationView, vararg viewsToShow: View) {
        lottieView.cancelAnimation()
        lottieView.visibility = View.GONE
        viewsToShow.forEach { it.visibility = View.VISIBLE }
    }
}
