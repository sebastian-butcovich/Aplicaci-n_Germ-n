package com.example.proyecto_german

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_german.Fragments.FormFragmentProfundidad
import com.example.proyecto_german.ViewModel.PerforacionViewModel
import com.example.proyecto_german.databinding.ActivityMainBinding
import com.example.proyecto_german.databinding.FragmentFormularioPerforacionBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        controladorDeLaNavegacion()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun controladorDeLaNavegacion() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setOnItemSelectedListener { item ->
            val currentId = navController.currentDestination?.id
            when (item.itemId) {
                R.id.menu_home -> {
                    if (currentId != R.id.homeFragment) {
                        navController.navigate(R.id.homeFragment)
                    }
                    true
                }

                R.id.menu_registro -> {
                    if (currentId != R.id.formFragment &&
                        currentId != R.id.formFragmentProfundidad &&
                        currentId != R.id.formFragmentSTP
                    ) {
                        navController.navigate(R.id.formFragment)
                    }
                    true
                }

                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.navView.selectedItemId = R.id.menu_home
                }

                R.id.formFragment -> {
                    binding.navView.selectedItemId = R.id.menu_registro
                    val item = binding.navView.menu.findItem(R.id.menu_registro)
                    item.setIcon(R.drawable.registrar)
                    item.title = "Registro de perforación"
                }

                R.id.formFragmentProfundidad -> {
                    binding.navView.selectedItemId = R.id.menu_registro
                    val item = binding.navView.menu.findItem(R.id.menu_registro)
                    item.setIcon(R.drawable.lista_icon)
                    item.title = "Lista STP"
                }

                R.id.formFragmentSTP -> {
                    binding.navView.selectedItemId = R.id.menu_registro
                    val item = binding.navView.menu.findItem(R.id.menu_registro)
                    item.setIcon(R.drawable.form_icon_stp)
                    item.title = "Registro STP"
                }
            }
        }
    }

}