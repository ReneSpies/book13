package co.aresid.book13

import android.graphics.drawable.AnimatedVectorDrawable
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber


/**
 *    Created on: 20/09/2020
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

object Util {

    fun TextInputLayout.showErrorMessage(message: String) {

        Timber.d("showErrorMessage: called")

        error = message

    }

    fun MaterialButton.showLoadingSpinnerAndDisable() {

        Timber.d("showLoadingSpinnerAndDisable: called")

        // Get the loading circle from the drawables
        val animatedLoadingSpinner = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.animated_loading_spinner,
            null
        ) as AnimatedVectorDrawable

        // Disable the button
        isEnabled = false

        // Start the animation
        animatedLoadingSpinner.start()

        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, animatedLoadingSpinner, null)

    }

}