package co.aresid.book13.fragments.addbook

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.aresid.book13.R
import co.aresid.book13.Util.showErrorMessage
import co.aresid.book13.databinding.FragmentAddBookBinding
import com.google.android.material.textfield.TextInputLayout
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
	
	// Binding for the layout
	private lateinit var binding: FragmentAddBookBinding
	
	// Corresponding ViewModel
	private lateinit var addBookViewModel: AddBookViewModel
	
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
		
		// Define the ViewModel
		addBookViewModel = ViewModelProvider(this).get(AddBookViewModel::class.java)
		
		// Tell the layout about the ViewModel
		binding.viewModel = addBookViewModel
		
		// Observe the showStartDatePickerDialog LiveData
		addBookViewModel.showStartDatePickerDialog.observe(viewLifecycleOwner,
		                                                   { shouldShow ->
			
			                                                   if (shouldShow) {
				
				                                                   showStartDatePickerDialog()
				
			                                                   }
			
		                                                   })
		
		// Observe the showFinishDatePickerDialog LiveData
		addBookViewModel.showFinishDatePickerDialog.observe(viewLifecycleOwner,
		                                                    { shouldShow ->
			
			                                                    if (shouldShow) {
				
				                                                    showFinishDatePickerDialog()
				
			                                                    }
			
		                                                    })
		
		// Observe the editTextErrors LiveData
		addBookViewModel.editTextErrors.observe(viewLifecycleOwner,
		                                        { state ->
			
			                                        when (state) {
				
				                                        EditTextErrors.INIT -> removeAllEditTextErrors()
				
				                                        EditTextErrors.BOOK_TITLE_MISSING -> {
					
					                                        binding.bookTitleLayout.showErrorMessage(requireContext().getString(R.string.book_title_missing))
					
				                                        }
				
				                                        EditTextErrors.BOOK_AUTHOR_MISSING -> {
					
					                                        binding.bookAuthorLayout.showErrorMessage(requireContext().getString(R.string.book_author_missing))
					
				                                        }
				
				                                        EditTextErrors.BOOK_PAGE_COUNT_MISSING -> {
					
					                                        binding.bookPageCountLayout.showErrorMessage(requireContext().getString(R.string.book_page_count_missing))
					
				                                        }
				
				                                        else -> {
				                                        }
				
			                                        }
			
		                                        })
		
		// Return the inflated layout
		return binding.root
		
	}
	
	private fun removeAllEditTextErrors() {
		
		Timber.d("removeAllEditTextErrors: called")
		
		for (position in 0 .. binding.constraintLayout.childCount) {
			
			val view = binding.constraintLayout.getChildAt(position)
			
			if (view is TextInputLayout) {
				
				view.error = null
				
				view.isErrorEnabled = false
				
			}
			
		}
		
	}
	
	private val startDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
		
		val calendar = Calendar.getInstance()
		calendar.set(
			year,
			month,
			day
		)
		
		binding.startDateTextView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
		
		addBookViewModel.bookStartDateInMilliseconds = calendar.timeInMillis
		
	}
	
	private val finishDatePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
		
		val calendar = Calendar.getInstance()
		calendar.set(
			year,
			month,
			day
		)
		
		binding.finishDateTextView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
		
		addBookViewModel.bookFinishDateInMilliseconds = calendar.timeInMillis
		
	}
	
	private fun showStartDatePickerDialog() {
		
		Timber.d("showStartDatePickerDialog: called")
		
		val calendar = Calendar.getInstance()
		val year = calendar.get(Calendar.YEAR)
		val month = calendar.get(Calendar.MONTH)
		val day = calendar.get(Calendar.DAY_OF_MONTH)
		
		// Create and immediately show the dialog
		val datePickerFragment = DatePickerDialog(
			requireContext(),
			android.R.style.Theme_Material_Dialog_Alert,
			startDatePickerListener,
			year,
			month,
			day
		)
		datePickerFragment.show()
		
		// Reset the LiveData
		addBookViewModel.startDatePickerDialogShown()
		
	}
	
	private fun showFinishDatePickerDialog() {
		
		Timber.d("showFinishDatePickerDialog: called")
		
		val calendar = Calendar.getInstance()
		val year = calendar.get(Calendar.YEAR)
		val month = calendar.get(Calendar.MONTH)
		val day = calendar.get(Calendar.DAY_OF_MONTH)
		
		// Create and immediately show the dialog
		val datePickerFragment = DatePickerDialog(
			requireContext(),
			android.R.style.Theme_Material_Dialog_Alert,
			finishDatePickerListener,
			year,
			month,
			day
		)
		datePickerFragment.show()
		
		// Reset the LiveData
		addBookViewModel.finishDatePickerDialogShown()
		
	}
	
}