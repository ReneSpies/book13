package co.aresid.book13.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import co.aresid.book13.R
import co.aresid.book13.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

	// Binding for the layout
	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)

		// Define the binding and inflate the layout
		binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

		// Inflate the menu for the SpeedDial
		binding.speedDialView.inflate(R.menu.speed_dial)

		val navigationHostFragment =
			supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
		val navigationController = navigationHostFragment.navController

		binding.speedDialView.setOnActionSelectedListener { actionItem ->

			when (actionItem.id) {

				R.id.add_book -> {

					navigationController.navigate(R.id.addBookFragment)

				}

				R.id.start_tracking -> {

					navigationController.navigate(R.id.trackingDetailsFragment)

				}

			}

			false

		}

		// Set the inflated layout
		setContentView(binding.root)

	}

}