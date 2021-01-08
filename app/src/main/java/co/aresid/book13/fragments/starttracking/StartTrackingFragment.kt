package co.aresid.book13.fragments.starttracking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
	
	// Binding for the layout
	private lateinit var binding: FragmentStartTrackingBinding
	
	// Corresponding ViewModel
	private lateinit var startTrackingViewModel: StartTrackingViewModel
	
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
		
		// Define the ViewModel
		startTrackingViewModel = ViewModelProvider(this).get(StartTrackingViewModel::class.java)
		
		// Tell the layout about the ViewModel
		binding.viewModel = startTrackingViewModel
		
		startTrackingViewModel.booksAutoCompleteTextViewAdapter.observe(viewLifecycleOwner,
		                                                                {
			
			                                                                binding.bookTitleAutoCompleteTextView.setAdapter(it)
			
		                                                                })
		
		startTrackingViewModel.showStartDatePickerDialog.observe(viewLifecycleOwner,
		                                                         { shouldShow ->
			
			                                                         if (shouldShow) showStartDatePickerDialog()
			
		                                                         })
		
		startTrackingViewModel.showFinishDatePickerDialog.observe(viewLifecycleOwner,
		                                                          { shouldShow ->
			
			                                                          if (shouldShow) showFinishDatePickerDialog()
			
		                                                          })
		
		startTrackingViewModel.showEditTextErrors.observe(viewLifecycleOwner,
		                                                  { error ->
			
			                                                  when (error) {
				
				                                                  EditTextErrors.INIT -> resetEditTextErrors()
				
				                                                  EditTextErrors.NO_BOOK_FOUND -> showNoBookFoundError()
				
				                                                  EditTextErrors.BOOK_TITLE_MISSING -> showBookTitleMissingError()
				
				                                                  EditTextErrors.START_PAGE_COUNT_MISSING -> showStartPageCountMissingError()
				
				                                                  EditTextErrors.FINISH_PAGE_COUNT_MISSING -> showFinishPageCountMissingError()
				
				                                                  else -> {
				                                                  }
				
			                                                  }
			
		                                                  })
		
		// Return the inflated layout
		return binding.root
		
	}
	
	private fun resetEditTextErrors() {
		
		Timber.d("resetEditTextErrors: called")
		
	}
	
	private fun showNoBookFoundError() {
		
		Timber.d("showNoBookFoundError: called")
		
		binding.bookTitleLayout.error = "No book found"
		
	}
	
	private fun showBookTitleMissingError() {
		
		Timber.d("showBookTitleMissingError: called")
		
		binding.bookTitleLayout.error = "Book title missing"
		
	}
	
	private fun showStartPageCountMissingError() {
		
		Timber.d("showStartPageCountMissingError: called")
		
		binding.bookTitleLayout.error = "Starting page count missing"
		
	}
	
	private fun showFinishPageCountMissingError() {
		
		Timber.d("showFinishPageCountMissingError: called")
		
		binding.bookTitleLayout.error = "Finishing page count missing"
		
	}
	
	private val startDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
		
		val calendar = Calendar.getInstance()
		calendar.set(
			year,
			month,
			day
		)
		
		binding.startDateTextView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
		
		startTrackingViewModel.trackingStartDateInMilliseconds = calendar.timeInMillis
		
	}
	
	private val finishDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
		
		val calendar = Calendar.getInstance()
		calendar.set(
			year,
			month,
			day
		)
		
		binding.finishDateTextView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
		
		startTrackingViewModel.trackingFinishDateInMilliseconds = calendar.timeInMillis
		
	}
	
	private fun showStartDatePickerDialog() {
		
		Timber.d("showStartDatePickerDialog: called")
		
		val calendar = Calendar.getInstance()
		val day = calendar.get(Calendar.DAY_OF_MONTH)
		val month = calendar.get(Calendar.MONTH)
		val year = calendar.get(Calendar.YEAR)
		
		val datePickerFragment = DatePickerDialog(
			requireContext(),
			android.R.style.Theme_Material_Dialog_Alert,
			startDatePickerListener,
			year,
			month,
			day
		)
		
		datePickerFragment.show()
		
		startTrackingViewModel.startDatePickerDialogShown()
		
	}
	
	private fun showFinishDatePickerDialog() {
		
		Timber.d("showFinishDatePickerDialog: called")
		
		val calendar = Calendar.getInstance()
		val day = calendar.get(Calendar.DAY_OF_MONTH)
		val month = calendar.get(Calendar.MONTH)
		val year = calendar.get(Calendar.YEAR)
		
		val datePickerFragment = DatePickerDialog(
			requireContext(),
			android.R.style.Theme_Material_Dialog_Alert,
			finishDatePickerListener,
			year,
			month,
			day
		)
		
		datePickerFragment.show()
		
		startTrackingViewModel.finishDatePickerDialogShown()
		
	}
	
}