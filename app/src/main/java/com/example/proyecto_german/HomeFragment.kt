package com.example.proyecto_german

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyecto_german.databinding.FragmentHomeBinding


class HomeFragment: Fragment() {
    private var _biding : FragmentHomeBinding? =null
    private val binding get() = _biding!!
     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _biding = FragmentHomeBinding.inflate(inflater,container,false)
         return binding.root
    }
}