package com.bapinaev.flighttracker.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.databinding.FragmentSearchBinding
import java.util.Locale

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        binding.btnFind.setOnClickListener {
            val origin = binding.etOrigin.text?.toString()?.trim().orEmpty().uppercase(Locale.getDefault())
            val dest = binding.etDestination.text?.toString()?.trim().orEmpty().uppercase(Locale.getDefault())
            val date = binding.etDate.text?.toString()?.trim().orEmpty()
            val direct = binding.checkboxDirect.isChecked

            val directText = if (direct) "прямой" else "с пересадками"
            binding.tvResult.text = "Маршрут: $origin → $dest\nДата: $date\nТип: $directText"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
