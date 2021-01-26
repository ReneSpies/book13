package co.aresid.book13.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import co.aresid.book13.R
import co.aresid.book13.databinding.ActivityMainBinding
import com.leinardi.android.speeddial.SpeedDialActionItem
import timber.log.Timber

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Binding for the layout

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        binding =
            ActivityMainBinding.inflate(LayoutInflater.from(this)) // Define the binding and inflate the layout

        createSpeedDial() // Create the FloatingActionButton's SpeedDial

        setContentView(binding.root) // Render the inflated layout

    }

    /**
     * Creates the FloatingActionButton's SpeedDial.
     */
    private fun createSpeedDial() {

        Timber.d("createSpeedDial: called")

        val speedDial = binding.speedDialView // Get the SpeedDial from data binding

        // Create the startTracking ActionItem for the SpeedDial
        val startTrackingSpeedDialActionItem = SpeedDialActionItem.Builder(
            R.id.start_tracking,
            R.drawable.ic_sharp_timer_24
        ).setFabImageTintColor(
            ContextCompat.getColor(
                this,
                android.R.color.white
            )
        ).setLabel(R.string.start_tracking).create()

        // Create the addBook ActionItem for the SpeedDial
        val addBookSpeedDialActionItem = SpeedDialActionItem.Builder(
            R.id.add_book,
            R.drawable.ic_sharp_menu_book_24
        ).setFabImageTintColor(
            ContextCompat.getColor(
                this,
                android.R.color.white
            )
        ).setLabel(R.string.add_a_book).create()

        // Gather above created ActionItems in a list
        val speedDialActionItemCollection = listOf(
            startTrackingSpeedDialActionItem,
            addBookSpeedDialActionItem
        )

        speedDial.addAllActionItems(speedDialActionItemCollection) // Add all ActionItems from above list to the SpeedDial

        createSpeedDialClickListener() // Create the ClickListener for the SpeedDial and its ActionItems

    }

    /**
     * Creates the ClickListener for the SpeedDial and its ActionItems.
     */
    private fun createSpeedDialClickListener() {

        Timber.d("createSpeedDialClickListener: called")

        val navigationHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment // Find the NavigationHostFragment
        val navigationController =
            navigationHostFragment.navController // Collect the NavController from the NavigationHostFragment

        // Create and set the OnActionSelectedListener on the SpeedDial
        binding.speedDialView.setOnActionSelectedListener { actionItem ->

            // Build a NavOptions object with the appropriate animations and popUpTo destination
            val navigationOptions = NavOptions.Builder().setPopUpTo(
                R.id.trackingListFragment, // PopUpTo destination
                false // We do not want the trackingListFragment to be popped as well
            ).setEnterAnim(R.anim.fade_in).setPopEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out).setPopExitAnim(R.anim.fade_out).build()

            // Check the actionItem's id and navigate accordingly
            when (actionItem.id) {

                // Navigate to addBookFragment
                R.id.add_book -> {

                    navigationController.navigate(
                        R.id.addBookFragment,
                        null,
                        navigationOptions
                    )

                }

                // Navigate to startTrackingFragment
                R.id.start_tracking -> {

                    navigationController.navigate(
                        R.id.startTrackingFragment,
                        null,
                        navigationOptions
                    )

                }

            }

            false

        }

    }

}