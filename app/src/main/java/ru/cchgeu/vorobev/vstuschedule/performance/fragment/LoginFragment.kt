package ru.cchgeu.vorobev.vstuschedule.performance.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.cchgeu.vorobev.vstuschedule.R
import ru.cchgeu.vorobev.vstuschedule.databinding.FragmentLoginBinding
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.MainViewModel
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.UiState
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel.waitState()
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logInButton.setOnClickListener {
            mainViewModel.login(
                binding.logInLoginTv.text.toString(),
                binding.logInPasswordTv.text.toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.state.observe(this, Observer {
            when (it) {
                is UiState.Loading -> {}
                UiState.InvalidLoginOrPass -> {
                    Snackbar.make(binding.root,"Не правильный огин или пароль", Snackbar.LENGTH_LONG).setAnchorView(binding.logInButton).show()
                    mainViewModel.waitState()
                    }
                UiState.Succes -> {
                    binding.root.findNavController().navigate(R.id.action_login_to_scheduleFragment)
                }
                UiState.Wait -> {}
                UiState.Unathorized -> {}
                UiState.DataError -> {}
                UiState.NetworkError -> {
                    Snackbar.make(binding.root,"Ошибка подключения к сети",Snackbar.LENGTH_LONG).setAnchorView(binding.logInButton).show()
                    mainViewModel.waitState()
                }

                UiState.PreSucces -> {}
            }
        })
    }
}