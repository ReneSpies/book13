package co.aresid.book13.fragments.trackinglist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import timber.log.Timber


/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */


class TrackingListViewModel(application: Application) : AndroidViewModel(application) {

    init {

        Timber.d("init: called")

    }

}