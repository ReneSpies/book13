@file:Suppress("unused")

package co.aresid.book13.application

import android.app.Application
import timber.log.Timber

/**
 *    Created on: 8/14/20
 *    For Project: book13
 *    Author: René Spies
 *    Copyright: © 2020 ARES ID
 */

class Book13Application: Application() {
	
	/*
	ONLY EDIT IF NECESSARY!
	 */
	
	override fun onCreate() {
		
		super.onCreate()
		
		Timber.plant(Timber.DebugTree()) // Launch Timber for logging
		
	}
}