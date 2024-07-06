package com.example.apppaciente.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.apppaciente.R
import com.example.apppaciente.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val nombreUsuario = sharedPreferences.getString("nombreUsuario", "")
        val ape_completo = sharedPreferences.getString("ape_completo", "")


        binding.txtNombreUsuario.text = "$nombreUsuario $ape_completo"

        binding.btnCitasProgramadas.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_citasProgramadasFragment)
        }

        binding.btnPagarCita.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_pagoPendientesFragment)
        }
        binding.btnPagarCita2.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_pagoPendientesFragment2)
        }

        binding.btnAgregarCitaHome.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_agregarCitaFragment)
        }

        binding.btnSeguroDental.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_segurosDentalesFragment)
        }
        binding.btnNotificacion.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_notificacionesFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}