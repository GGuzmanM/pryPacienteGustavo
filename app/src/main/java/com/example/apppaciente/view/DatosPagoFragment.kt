package com.example.apppaciente.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.apppaciente.databinding.FragmentDatosPagoBinding

class DatosPagoFragment : Fragment() {

    private var _binding: FragmentDatosPagoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatosPagoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = DatosPagoFragmentArgs.fromBundle(requireArguments())
        binding.etMontoTotalDatoPago.text = args.montoTotal
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
