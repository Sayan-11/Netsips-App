package `in`.netsips.netsipsapp

import `in`.netsips.netsipsapp.databinding.ActivityMainBinding
import `in`.netsips.netsipsapp.ui.NotificationsFragment
import `in`.netsips.netsipsapp.ui.archive.ArchiveFragment
import `in`.netsips.netsipsapp.ui.home.HomeFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.main_activity_frame,
            HomeFragment()
        )
            .commit()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_navigation_archive -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_activity_frame,
                            ArchiveFragment()
                        ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_navigation_notifications -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_activity_frame, NotificationsFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottom_navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_activity_frame,
                            HomeFragment()
                        ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.main_activity_frame,
                            HomeFragment()
                        ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
    }
}
