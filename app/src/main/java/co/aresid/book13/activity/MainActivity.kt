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

class MainActivity : AppCompatActivity() {

    // Binding for the layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Define the binding and inflate the layout
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        createSpeedDial()

        // Set the inflated layout
        setContentView(binding.root)

    }

    private fun createSpeedDial() {

        Timber.d("createSpeedDial: called")

        val speedDial = binding.speedDialView

        val startTrackingSpeedDialActionItem = SpeedDialActionItem.Builder(
            R.id.start_tracking,
            R.drawable.ic_sharp_timer_24
        ).setFabImageTintColor(ContextCompat.getColor(this, android.R.color.white))
            .setLabel(R.string.start_tracking).create()

        val addBookSpeedDialActionItem = SpeedDialActionItem.Builder(
            R.id.add_book,
            R.drawable.ic_sharp_menu_book_24
        ).setFabImageTintColor(ContextCompat.getColor(this, android.R.color.white))
            .setLabel(R.string.add_a_book).create()

        val speedDialActionItemCollection = listOf(
            startTrackingSpeedDialActionItem,
            addBookSpeedDialActionItem
        )

        speedDial.addAllActionItems(speedDialActionItemCollection)

        createSpeedDialClickListener()

    }

    private fun createSpeedDialClickListener() {

        Timber.d("createSpeedDialClickListener: called")

        val navigationHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        val navigationController = navigationHostFragment.navController

        binding.speedDialView.setOnActionSelectedListener { actionItem ->

            val navigationOptions = NavOptions.Builder().setPopUpTo(
                R.id.trackingListFragment,
                false
            ).setEnterAnim(R.anim.fade_in).setPopEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out).setPopExitAnim(R.anim.fade_out).build()

            when (actionItem.id) {

                R.id.add_book -> {

                    navigationController.navigate(
                        R.id.addBookFragment,
                        null,
                        navigationOptions
                    )

                }

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