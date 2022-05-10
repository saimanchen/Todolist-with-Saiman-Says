@file:Suppress("DEPRECATION")

package com.saisai.taskmanagersaimansays.fragments.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.saisai.taskmanagersaimansays.R
import com.saisai.taskmanagersaimansays.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        binding.apply {

            // after 3 seconds, navigate from SplashFragment to Listfragment
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashFragment2_to_listFragment)
            },3000)

        }
        return binding.root
    }
}