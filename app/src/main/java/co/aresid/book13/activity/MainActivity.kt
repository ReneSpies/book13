package co.aresid.book13.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import co.aresid.book13.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

	// Binding for the layout
	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {

		super.onCreate(savedInstanceState)

		// Define the binding and inflate the layout
		binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

		// Set the inflated layout
		setContentView(binding.root)

	}

}