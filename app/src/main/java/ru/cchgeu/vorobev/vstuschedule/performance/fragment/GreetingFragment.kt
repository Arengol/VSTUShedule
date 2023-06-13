package ru.cchgeu.vorobev.vstuschedule.performance.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.cchgeu.vorobev.vstuschedule.R
import ru.cchgeu.vorobev.vstuschedule.databinding.FragmentGreetingBinding
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.MainViewModel
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.UiState
import javax.inject.Inject

@AndroidEntryPoint
class GreetingFragment : Fragment() {
    @Inject
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentGreetingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel.waitState()
        mainViewModel.userDataCheck()
        binding = FragmentGreetingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.greetingLogInButton.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_greetingFragment_to_login)
        }
        binding.studentSignInButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isStudent", true)
            binding.root.findNavController().navigate(R.id.action_greetingFragment_to_signInFragment, bundle)
        }
        binding.mentorSignInButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("isStudent", false)
            binding.root.findNavController().navigate(R.id.action_greetingFragment_to_signInFragment, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.state.observe(this, Observer {
            when (it) {
                UiState.Loading -> {}
                UiState.InvalidLoginOrPass -> {}
                UiState.Succes -> {
                    binding.root.findNavController().navigate(R.id.action_greetingFragment_to_scheduleFragment)
                }
                UiState.Wait -> {}
                UiState.Unathorized -> {}
                UiState.DataError -> {}
                UiState.NetworkError -> {}
                UiState.PreSucces -> {}
            }
        })
    }
}