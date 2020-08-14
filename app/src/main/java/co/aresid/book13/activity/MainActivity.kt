package co.aresid.book13.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import co.aresid.book13.R
import co.aresid.book13.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

	// Binding for the layout
	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)

		// Define the binding and inflate the layout
		binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

		// TODO: 8/14/20 Change speed dial's items backgroundColor or iconTint
		binding.speedDialView.inflate(R.menu.speed_dial)

		// Set the inflated layout
		setContentView(binding.root)

	}

}