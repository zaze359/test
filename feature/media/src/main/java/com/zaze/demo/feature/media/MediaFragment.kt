package com.zaze.demo.feature.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zaze.demo.feature.media.databinding.FragmentMediaBinding


/**
 * 主UI
 */
class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MediaFragment_to_VideoFragment)
        }
        binding.audioBtn.setOnClickListener {
            findNavController().navigate(R.id.action_MediaFragment_to_AudioFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}