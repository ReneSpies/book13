package co.aresid.book13

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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
	
	fun MaterialButton.disableAndShowLoadingSpinner() {
		
		Timber.d("showLoadingSpinnerAndDisable: called")
		
		// First, disable the button
		isEnabled = false
		
		// Get the loading circle from the drawables
		val animatedLoadingSpinner = ResourcesCompat.getDrawable(
			context.resources,
			R.drawable.animated_loading_spinner,
			null
		) as AnimatedVectorDrawable
		
		// Start the animation
		animatedLoadingSpinner.start()
		
		// Put the animatedLoadingSpinner on the button
		setCompoundDrawablesRelativeWithIntrinsicBounds(
			null,
			null,
			animatedLoadingSpinner,
			null
		)
		
	}
	
	fun MaterialButton.enableAndShowCheckFor500Millis() {
		
		Timber.d("enableAndShowCheckFor500Millis: called")
		
		// First, enable the button
		isEnabled = true
		
		// Get the 'check' drawable
		val check = ResourcesCompat.getDrawable(
			context.resources,
			R.drawable.ic_sharp_check_24,
			null
		)
		
		// Put the 'check' drawable on the button
		setCompoundDrawablesRelativeWithIntrinsicBounds(
			null,
			null,
			check,
			null
		)
		
		// Reset the compound drawables after 500 milliseconds
		Executors.newSingleThreadScheduledExecutor().schedule(
			{
				enableAndResetCompoundDrawablesWithIntrinsicBounds()
			},
			500,
			TimeUnit.MILLISECONDS
		)
		
	}
	
	fun MaterialButton.enableAndResetCompoundDrawablesWithIntrinsicBounds() {
		
		Timber.d("resetCompoundDrawablesWithIntrinsicBounds: called")
		
		// First, enable the button
		isEnabled = true
		
		// Reset the drawables
		setCompoundDrawablesRelativeWithIntrinsicBounds(
			null,
			null,
			null,
			null
		)
		
	}
	
	fun View.showErrorSnackbar(message: String) {
		
		Timber.d("showErrorSnackbar: called")
		
		Snackbar.make(
			this,
			message,
			Snackbar.LENGTH_LONG
		).setBackgroundTint(
			ContextCompat.getColor(
				context,
				R.color.errorColor
			)
		).setTextColor(
			ContextCompat.getColor(
				context,
				android.R.color.white
			)
		).show()
		
	}
	
}