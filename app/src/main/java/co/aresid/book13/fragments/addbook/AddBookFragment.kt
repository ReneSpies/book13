package co.aresid.book13.fragments.addbook

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.aresid.book13.R
import co.aresid.book13.Util.DatePickerVariant
import co.aresid.book13.Util.TextInputLayoutErrors
import co.aresid.book13.Util.isValidAndNotInit
import co.aresid.book13.Util.resetAllTextInputLayoutErrors
import co.aresid.book13.Util.showErrorMessage
import co.aresid.book13.databinding.FragmentAddBookBinding
import timber.log.Timber
import java.text.DateFormat
import java.util.*

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class AddBookFragment: Fragment() {
	
	private lateinit var binding: FragmentAddBookBinding // Binding for the layout
	
	private lateinit var addBookViewModel: AddBookViewModel // Corresponding ViewModel
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		Timber.d("onCreateView: called")
		
		// Define the binding and inflate the layout
		binding = FragmentAddBookBinding.inflate(
			inflater,
			container,
			false
		)
		
		addBookViewModel = ViewModelProvider(this).get(AddBookViewModel::class.java) // Define the ViewModel
		
		binding.viewModel = addBookViewModel // Tell the binding about the ViewModel to use its methods in XML
		
		// Observe the showStartDatePickerDialog LiveData
		addBookViewModel.renderDatePickerDialog.observe(viewLifecycleOwner,
		                                                { variant ->
			
			                                                renderDatePickerDialog(variant)
			
		                                                })
		
		// Observe the editTextErrors LiveData
		addBookViewModel.editTextErrors.observe(viewLifecycleOwner,
		                                        { state ->
			
			                                        when (state) {
				
				                                        TextInputLayoutErrors.DEFAULT -> binding.constraintLayout.resetAllTextInputLayoutErrors()
				
				                                        TextInputLayoutErrors.BOOK_TITLE_MISSING -> binding.bookTitleLayout.showErrorMessage(requireContext().getString(R.string.book_title_missing))
				
				                                        TextInputLayoutErrors.BOOK_AUTHOR_MISSING -> binding.bookAuthorLayout.showErrorMessage(requireContext().getString(R.string.book_author_missing))
				
				                                        TextInputLayoutErrors.BOOK_PAGE_COUNT_MISSING -> binding.bookPageCountLayout.showErrorMessage(requireContext().getString(R.string.book_page_count_missing))
				
				                                        else -> {
				                                        }
				
			                                        }
			
		                                        })
		
		// Observe the  LiveData
		addBookViewModel.clearAllEditTextFields.observe(viewLifecycleOwner,
		                                                {
				
				                                                shouldClear ->
			
			                                                if (shouldClear) clearAllEditTextFields()
			
		                                                })
		
		return binding.root // Return the inflated layout to have it rendered
		
	}
	
	private fun clearAllEditTextFields() {
		
		Timber.d("clearAllEditTextFields: called")
		
		binding.bookTitleEditText.text = null
		binding.bookAuthorEditText.text = null
		binding.bookPageCountEditText.text = null
		
		addBookViewModel.allEditTextFieldsCleared() // Resets the LiveData
		
	}
	
	// Format the selected date to the users local format and render it on the screen as well as
	// pass the date in milliseconds to the ViewModel
	private val startDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
		
		// Create a Calendar instance from the selected year, month and day
		val calendar = Calendar.getInstance()
		calendar.set(
			year,
			month,
			day
		)
		
		binding.startDateTextView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time) // Format date to local format and render it on the screen
		
		addBookViewModel.bookStartDateInMilliseconds = calendar.timeInMillis // Pass the date in milliseconds to the ViewModel
		
	}
	
	// Format the selected date to the users local format and render it on the screen as well as
	// pass the date in milliseconds to the ViewModel
	private val finishDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
		
		// Create a Calendar instance from the selected year, month and day
		val calendar = Calendar.getInstance()
		calendar.set(
			year,
			month,
			day
		)
		
		binding.finishDateTextView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time) // Format date to local format and render it on the screen
		
		addBookViewModel.bookFinishDateInMilliseconds = calendar.timeInMillis // Pass the date in milliseconds to the ViewModel
		
	}
	
	/**
	 * Gets the current date, creates a [DatePickerDialog] object from the current date,
	 * renders it and resets the LiveData that initiated the render.
	 */
	private fun renderDatePickerDialog(variant: DatePickerVariant) {
		
		Timber.d("renderDatePickerDialog: called")
		
		// Check if the given parameter is invalid
		if (!variant.isValidAndNotInit()) {
			
			return
			
		}
		
		val datePickerListener = if (variant == DatePickerVariant.START) startDatePickerListener else finishDatePickerListener
		
		// Get the current year, month and day from a Calendar instance
		val calendar = Calendar.getInstance()
		val year = calendar.get(Calendar.YEAR)
		val month = calendar.get(Calendar.MONTH)
		val day = calendar.get(Calendar.DAY_OF_MONTH)
		
		// Create the dialog
		val datePickerFragment = DatePickerDialog(
			requireContext(),
			android.R.style.Theme_Material_Dialog_Alert,
			datePickerListener,
			year,
			month,
			day
		)
		datePickerFragment.show() // Render the dialog
		
		addBookViewModel.datePickerDialogShown() // Reset the LiveData
		
	}
	
}