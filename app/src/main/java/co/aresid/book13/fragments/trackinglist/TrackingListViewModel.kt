package co.aresid.book13.fragments.trackinglist

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import co.aresid.book13.R
import co.aresid.book13.Util.renderErrorSnackbar
import co.aresid.book13.database.trackingdata.TrackingData
import co.aresid.book13.recyclerview.TrackingListViewHolder
import co.aresid.book13.repository.Book13Repository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class TrackingListViewModel(application: Application): AndroidViewModel(application) {

    // This LiveData carries the ItemTouchHelper.SimpleCallback information
    private val _itemTouchHelperCallback = MutableLiveData<ItemTouchHelper.SimpleCallback>()
    val itemTouchHelperCallback: LiveData<ItemTouchHelper.SimpleCallback>
        get() = _itemTouchHelperCallback

    val allTrackingData: LiveData<List<TrackingData>>
        get() {

            val repository = Book13Repository.getInstance(getApplication())

            return repository.getAllTrackingDataLiveData()

        }

    init {

        Timber.d("init: called")

        // Initialize LiveData
        loadDefaultItemTouchHelperCallbackValue()

    }

    /**
     * Deletes [trackingData] from the database using the [Book13Repository] and its method [Book13Repository.deleteTrackingData].
     */
    private suspend fun deleteTrackingData(trackingData: TrackingData) {

        Timber.d("deleteTrackingData: called")

        val repository = Book13Repository.getInstance(getApplication()) // Get a repository instance

        repository.deleteTrackingData(trackingData) // Insert the trackingData into the database

    }

    /**
     * Inserts [trackingData] into the database using the [Book13Repository] and its method [Book13Repository.insertTrackingData].
     */
    private suspend fun insertTrackingData(trackingData: TrackingData) {

        Timber.d("insertTrackingData: called")

        val repository = Book13Repository.getInstance(getApplication()) // Get a repository instance

        repository.insertTrackingData(trackingData) // Insert the trackingData into the database

    }

    /**
     * Renders a Snackbar using [view].
     * The Snackbar has an action labeled "UNDO" which calls [block].
     */
    private suspend fun renderUndoSnackbar(view: View, block: suspend () -> Unit) {

        Timber.d("renderUndoSnackbar: called")

        val undoSnackbar = Snackbar.make(view, view.context.getString(R.string.entry_deleted), Snackbar.LENGTH_LONG) // Make a standard Snackbar

        // Give the Snackbar an action and callback the block suspend function
        undoSnackbar.setAction(view.context.getString(R.string.undo)) {

            viewModelScope.launch { block() }

        }

        undoSnackbar.show() // Render the Snackbar

    }

    /**
     * Loads the default value into [_itemTouchHelperCallback].
     * The default value is [ItemTouchHelper.SimpleCallback] which is overwritten in here.
     */
    private fun loadDefaultItemTouchHelperCallbackValue() {

        Timber.d("loadDefaultItemTouchHelperCallbackValue: called")

        // Load the default value and override the abstract class
        _itemTouchHelperCallback.value = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {

            // Not needed
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                Timber.d("onMove: called")

                return false

            }

            // Handles what happens when a RecyclerView item is swiped
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                Timber.d("onSwiped: called")

                viewHolder as TrackingListViewHolder // Cast the viewHolder to its proper class to access its variables

                val trackingData = viewHolder.binding.trackingData!! // Safe to assume the tracking data is not null because the RecyclerView only has items with trackingData
                val cardView = viewHolder.binding.cardView // Extract the item's cardView. Needed for context

                /**
                 * Renders an error Snackbar with the [R.string.standard_error_message] via [renderErrorSnackbar].
                 */
                fun handleException() {

                    Timber.d("handleException: called")

                    cardView.renderErrorSnackbar(cardView.context.getString(R.string.standard_error_message)) // Render an error snackbar with standard error message

                }

                // New CoroutineScope to do asynchronous work with the database
                viewModelScope.launch {

                    // Catch Exceptions thrown when deleting the trackingData on swipe
                    try {

                        deleteTrackingData(trackingData) // Delete the trackingData

                        // Render a Snackbar with an UNDO action
                        renderUndoSnackbar(cardView) {

                            // Catch Exception thrown when re-inserting the deleted trackingData
                            try {

                                insertTrackingData(trackingData) // Re-insert the deleted trackingData

                            } catch (exception: Exception) {

                                Timber.e(exception)

                                handleException() // Handle the re-insert exceptions same as the delete exceptions

                            }

                        }

                    } catch (exception: Exception) {

                        Timber.e(exception)

                        handleException() // Handle the delete exceptions same as the re-insert exceptions

                    }

                }

            }

        }

    }

}