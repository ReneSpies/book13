package co.aresid.book13.fragments.starttracking

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
import co.aresid.book13.Util.isValidAndNotDefault
import co.aresid.book13.Util.resetAllTextInputLayoutErrors
import co.aresid.book13.database.trackingdata.TrackingData
import co.aresid.book13.databinding.FragmentStartTrackingBinding
import timber.log.Timber
import java.text.DateFormat
import java.util.*

/**
 *    Created on: 8/18/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class StartTrackingFragment: Fragment() {
	
	private lateinit var binding: FragmentStartTrackingBinding // Binding for the layout
	
	private lateinit var startTrackingViewModel: StartTrackingViewModel // Corresponding ViewModel
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		Timber.d("onCreateView: called")
		
		// Define the binding and inflate the layout
		binding = FragmentStartTrackingBinding.inflate(
			inflater,
			container,
			false
		)
		
		startTrackingViewModel = ViewModelProvider(this).get(StartTrackingViewModel::class.java) // Define the ViewModel
		
		binding.viewModel = startTrackingViewModel // Tell the layout about the ViewModel
		binding.lifecycleOwner = viewLifecycleOwner
		
		// Observe the booksAutoCompleteTextViewAdapter LiveData
		startTrackingViewModel.booksAutoCompleteTextViewAdapter.observe(
			viewLifecycleOwner,
			binding.bookTitleAutoCompleteTextView::setAdapter
		)
		
		// Observe the showStartDatePickerDialog LiveData
		startTrackingViewModel.renderDatePickerDialog.observe(
			viewLifecycleOwner,
			::renderDatePickerDialog
		)
		
		// Observe the showEditTextErrors LiveData
		startTrackingViewModel.renderEditTextErrors.observe(viewLifecycleOwner,
		                                                    { error ->
			
			                                                    when (error) {
				
				                                                    TextInputLayoutErrors.DEFAULT -> binding.constraintLayout.resetAllTextInputLayoutErrors()
				
				                                                    TextInputLayoutErrors.NO_BOOK_FOUND -> binding.bookTitleLayout.error = getString(R.string.no_book_found)
				
				                                                    TextInputLayoutErrors.BOOK_TITLE_MISSING -> binding.bookTitleLayout.error = getString(R.string.book_title_missing)
				
				                                                    TextInputLayoutErrors.START_PAGE_COUNT_MISSING -> binding.startingPageCountLayout.error = getString(R.string.starting_page_count_missing)
				
				                                                    TextInputLayoutErrors.FINISH_PAGE_COUNT_MISSING -> binding.finishingPageCountLayout.error = getString(R.string.finishing_page_count_missing)
				
				                                                    else -> {
				                                                    }
				
			                                                    }
			
		                                                    })
		
		return binding.root // Return the inflated layout
		
	}
	
	override fun onViewCreated(
		view: View,
		savedInstanceState: Bundle?
	) {
		
		Timber.d("onViewCreated: called")
		
		super.onViewCreated(
			view,
			savedInstanceState
		)
		
		val arguments = arguments
		
		arguments?.let { bundle ->
			
			val trackingData = bundle.getParcelable<TrackingData>("tracking data")
			
			trackingData?.let {
				
				startTrackingViewModel.populateFromTrackingData(trackingData)
				
			}
			
		}
		
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
		
		startTrackingViewModel.startDateInMilliseconds = calendar.timeInMillis // Pass the date in milliseconds to the ViewModel
		
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
		
		startTrackingViewModel.finishDateInMilliseconds = calendar.timeInMillis // Pass the date in milliseconds to the ViewModel
		
	}
	
	/**
	 * Gets the current date, creates a [DatePickerDialog] object from the current date,
	 * renders it and resets the LiveData that initiated the render.
	 */
	private fun renderDatePickerDialog(variant: DatePickerVariant) {
		
		Timber.d("renderDatePickerDialog: called")
		
		// Check if the given parameter is invalid
		if (!variant.isValidAndNotDefault()) {
			
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
		
		startTrackingViewModel.datePickerDialogShown() // Reset the LiveData
		
	}
	
}