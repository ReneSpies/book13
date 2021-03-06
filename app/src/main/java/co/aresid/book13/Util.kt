package co.aresid.book13

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import co.aresid.book13.database.bookdata.BookData
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.annotations.NotNull
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
	
	/**
	 * Gives the [RecyclerView]'s items a fixed size depending on [has].
	 */
	@JvmStatic
	@BindingAdapter("hasFixedSize")
	fun RecyclerView.hasFixedSize(@NotNull has: Boolean) {
		
		Timber.d("hasFixedSize: called")
		
		setHasFixedSize(has)
		
	}
	
	/**
	 * Checks if its list contains the [book]. Checks the fields
	 * [BookData.title], [BookData.author] and [BookData.numberOfPages].
	 *
	 * @return True if above fields are matching, false otherwise.
	 */
	fun List<BookData>.containsBook(book: BookData): Boolean {
		
		Timber.d("containsBook: called")
		
		for (storedBook in this) {
			
			if (book.title == storedBook.title && book.author == storedBook.author && book.numberOfPages == storedBook.numberOfPages) {
				
				return true
				
			}
			
		}
		
		return false
		
	}
	
	/**
	 * Uses the BindingAdapter annotation to make it available in the XML.
	 * Used to clear the text of multiple [EditText] views at once.
	 */
	@JvmStatic
	@BindingAdapter("clearText")
	fun EditText.clearText(@NotNull shouldClear: Boolean) {
		
		Timber.d("clearText: called")
		
		if (shouldClear) text = null
		
	}
	
	enum class TextInputLayoutErrors {
		
		/**
		 * This is used to initialize and reset the value.
		 */
		DEFAULT,
		
		/**
		 * This is used to render an error indicating that the book title's TextInputEditText value is missing.
		 */
		BOOK_TITLE_MISSING,
		
		/**
		 * This is used to render an error indicating that the book author's TextInputEditText value is missing.
		 */
		BOOK_AUTHOR_MISSING,
		
		/**
		 * This is used to render an error indicating that the book page count's TextInputEditText value is missing.
		 */
		BOOK_PAGE_COUNT_MISSING,
		
		/**
		 * This is used to render an error indicating that the book's TextInputEditText value is not found in the database.
		 */
		NO_BOOK_FOUND,
		
		/**
		 * This is used to render an error indicating that the tracking's start page count TextInputEditText value is missing.
		 */
		START_PAGE_COUNT_MISSING,
		
		/**
		 * This is used to render an error indicating that the tracking's finish page count TextInputEditText value is missing.
		 */
		FINISH_PAGE_COUNT_MISSING,
		
		/**
		 * This is used to render an error indicating that the given book already exists in the database.
		 */
		BOOK_ALREADY_EXISTS,
		
	}
	
	enum class DatePickerVariant {
		
		/**
		 * This is used to initialize and reset the value.
		 */
		DEFAULT,
		
		/**
		 * This is used to indicate the StartDatePicker variant.
		 */
		START,
		
		/**
		 * This is used to indicate the FinishDatePicker variant.
		 */
		FINISH
		
	}
	
	/**
	 * Iterates over all of its Views and resets the view's error if the View is of type [TextInputLayout].
	 */
	fun ViewGroup.resetAllTextInputLayoutErrors() {
		
		Timber.d("resetAllTextInputLayoutErrors: called")
		
		for (position in 0 .. childCount) {
			
			val view = getChildAt(position)
			
			if (view is TextInputLayout) {
				
				view.error = null // Reset the error
				
				view.isErrorEnabled = false // Disable the error to remove the space where it is rendered
				
			}
			
		}
		
	}
	
	/**
	 * Checks if [this] is either [DatePickerVariant.START] or
	 * [DatePickerVariant.FINISH].
	 *
	 * @return True if above expression is correct.
	 */
	fun DatePickerVariant.isValidAndNotDefault(): Boolean {
		
		Timber.d("isValidAndNotDefault: called")
		
		return (this == DatePickerVariant.START || this == DatePickerVariant.FINISH)
		
	}
	
	fun TextInputLayout.renderErrorMessage(message: String) {
		
		Timber.d("renderErrorMessage: called")
		
		error = message
		
	}
	
	fun MaterialButton.disableButtonAndRenderLoadingSpinner() {
		
		Timber.d("disableButtonAndRenderLoadingSpinner: called")
		
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
	
	fun MaterialButton.enableButtonAndShowCheckSignFor500Millis() {
		
		Timber.d("enableButtonAndShowCheckSignFor500Millis: called")
		
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
				enableButtonAndResetCompoundDrawablesWithIntrinsicBounds()
			},
			500,
			TimeUnit.MILLISECONDS
		)
		
	}
	
	fun MaterialButton.enableButtonAndResetCompoundDrawablesWithIntrinsicBounds() {
		
		Timber.d("enableButtonAndResetCompoundDrawablesWithIntrinsicBounds: called")
		
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
	
	fun View.renderErrorSnackbar(message: String) {
		
		Timber.d("renderErrorSnackbar: called")
		
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