package com.example.proyecto_german

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.proyecto_german.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var currentFragment: Fragment
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        //setupBottomNav()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//    private fun setupBottomNav() {
//        val homeFragment = HomeFragment()
//        val formFragment = FormFragment()
//        currentFragment=homeFragment
//        with(supportFragmentManager) {
//            beginTransaction()
//            .add(R.id.nav_host, formFragment)
//            .commit()
//            beginTransaction()
//                .add(R.id.nav_host, homeFragment)
//                .hide(formFragment).commit()
//            binding.navView.setOnItemSelectedListener { menuItem->
//                when(menuItem.itemId){
//                    R.id.navigation_home->{
//                        beginTransaction().hide(formFragment).show(homeFragment).commit()
//                        currentFragment = homeFragment
//                    }
//                    R.id.navigation_registro->{
//                        beginTransaction().hide(homeFragment).show(formFragment).commit()
//                        currentFragment = formFragment
//                    }
//                }
//                true
//            }
//        }
//    }
}