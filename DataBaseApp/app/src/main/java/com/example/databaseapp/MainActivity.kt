package com.example.databaseapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.databaseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(SharedPreferencesFragment())

        binding.bottomNavMenu.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.shared_preferences -> replaceFragment(SharedPreferencesFragment())
                R.id.room_database -> replaceFragment(RoomDatabaseFragment())
                R.id.data_store -> replaceFragment(DataStoreFragment())
                else -> {
                }
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
