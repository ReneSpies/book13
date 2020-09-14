package co.aresid.book13.fragments.addbook

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

class AddBookFragment : Fragment() {

    // Binding for the layout
    private lateinit var binding: FragmentAddBookBinding

    // Corresponding ViewModel
    private lateinit var addBookViewModel: AddBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.d("onCreateView: called")

        super.onCreateView(
            inflater,
            container,
            savedInstanceState
        )

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
        addBookViewModel.showStartDatePickerDialog.observe(viewLifecycleOwner, { shouldShow ->

            if (shouldShow) {

                showStartDatePickerDialog()

            }

        })

        // Observe the showFinishDatePickerDialog LiveData
        addBookViewModel.showFinishDatePickerDialog.observe(
            viewLifecycleOwner,
            { shouldShow ->

                if (shouldShow) {

                    showFinishDatePickerDialog()

                }

            })

        // Observe the showStartDateView LiveData
        addBookViewModel.showStartDateView.observe(viewLifecycleOwner, { shouldShow ->

            if (shouldShow) {

                showStartDateView()

            }

        })

        // Observe the showFinishDateView LiveData
        addBookViewModel.showFinishDateView.observe(viewLifecycleOwner, { shouldShow ->

            if (shouldShow) {

                // TODO: 14/09/2020 Show the view and hide the other

            }

        })

        // Return the inflated layout
        return binding.root

    }

    private fun showStartDateView() {

        Timber.d("showStartDateView: called")

        val localCalendar = Calendar.getInstance()
        localCalendar.timeInMillis = addBookViewModel.bookStartDate
        val startDate = localCalendar.time
        binding.startTextView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(startDate)
        binding.addStartDateButtonGroup.visibility = View.INVISIBLE
        binding.startDateGroup.visibility = View.VISIBLE

    }

    private val startDatePickerListener =
        DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

            Timber.d("onDateSet: called")
            Timber.d("date = $day.$month.$year")

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            addBookViewModel.bookStartDate = calendar.timeInMillis
            addBookViewModel.showStartDateView()

        }

    private val finishDatePickerListener =
        DatePickerDialog.OnDateSetListener { _, year, month, day ->

            Timber.d("onDateSet: called")
            Timber.d("date = $day.$month.$year")

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            addBookViewModel.bookFinishDate = calendar.timeInMillis
            addBookViewModel.showFinishDateView()

        }


    private fun showStartDatePickerDialog() {

        Timber.d("showStartDatePickerDialog: called")

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create and immediately show the dialog
        val datePickerFragment =
            DatePickerDialog(requireContext(), startDatePickerListener, year, month, day)
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
        val datePickerFragment =
            DatePickerDialog(requireContext(), finishDatePickerListener, year, month, day)
        datePickerFragment.show()

        // Reset the LiveData
        addBookViewModel.finishDatePickerDialogShown()

    }

}