package com.example.apppaciente.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.apppaciente.databinding.FragmentDetalleCitaBinding

class DetalleCitaFragment : Fragment() {

    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!
    private val args: DetalleCitaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Asignar los datos a los TextViews
        binding.tvEstado.text = args.estado
        binding.tvFecha.text = args.fecha
        binding.tvHora.text = args.hora
        binding.tvMotivoConsulta.text = args.motivoConsulta
        binding.tvNombreOdontologo.text = args.nombreOdontologo
        binding.tvDiagnostico.text = args.diagnostico
        binding.tvAnotacion.text = args.anotacion
        binding.tvCosto.text = args.costo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
