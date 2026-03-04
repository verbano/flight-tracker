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

            val directText = if (direct) {
                getString(R.string.search_type_direct)
            } else {
                getString(R.string.search_type_with_stops)
            }

            binding.tvResult.text = getString(
                R.string.search_result_template,
                origin,
                dest,
                date,
                directText
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
