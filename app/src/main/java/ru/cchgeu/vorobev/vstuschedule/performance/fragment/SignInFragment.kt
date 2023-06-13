package ru.cchgeu.vorobev.vstuschedule.performance.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.cchgeu.vorobev.vstuschedule.R
import ru.cchgeu.vorobev.vstuschedule.databinding.FragmentSignInBinding
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.MainViewModel
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.UiState
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {
    @Inject
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentSignInBinding
    private var isStudent: Boolean = true

    lateinit var userLogin: String
    lateinit var userPassword: String
    lateinit var userSecondPassword: String
    lateinit var inviteCode: String
    lateinit var linkData: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        isStudent = arguments?.getBoolean("isStudent") ?: true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isStudent) binding.inviteCodeTil.visibility = VISIBLE
        if (isStudent) {
            mainViewModel.getAllGroups()
            binding.accountLinkDataTil.hint = "\t\tГруппа"
        }
        else {
            mainViewModel.getAllMentors()
            binding.accountLinkDataTil.hint = "\t\tФИО"
        }
        binding.signInButton.setOnClickListener {
            val isLoginEmpty = binding.signInLoginTv.text?.isEmpty() ?: true
            val isPasswordEmpty = binding.signInPasswordTv.text?.isEmpty() ?: true
            val isSecondPasswordEmpty = binding.signInSecondPasswordTv.text?.isEmpty() ?: true
            val isLinkDataEmpty = binding.accountLinkDataTv.text?.isEmpty() ?: true
            if (isLoginEmpty || isPasswordEmpty || isSecondPasswordEmpty || isLinkDataEmpty)
                Snackbar.make(binding.root,"Не все поля заполнены", Snackbar.LENGTH_LONG).setAnchorView(binding.signInButton).show()
            else {
                userLogin = binding.signInLoginTv.text!!.toString()
                userPassword = binding.signInPasswordTv.text!!.toString()
                userSecondPassword = binding.signInSecondPasswordTv.text!!.toString()
                inviteCode = binding.inviteCodeTv.text?.toString() ?: ""
                linkData = binding.accountLinkDataTv.text!!.toString()
                if (!userPassword.equals(userSecondPassword))
                    Snackbar.make(binding.root,"Пароли не совпадают", Snackbar.LENGTH_LONG).setAnchorView(binding.signInButton).show()
                else {
                    mainViewModel.signup(userLogin, userPassword,
                        if (isStudent) 0 else 1,
                        inviteCode
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.state.observe(this, Observer {
            when (it) {
                is UiState.Loading -> {}
                UiState.InvalidLoginOrPass -> {
                    Snackbar.make(binding.root, "Некорректные данные", Snackbar.LENGTH_LONG).setAnchorView(binding.signInButton).show()
                    mainViewModel.waitState()
                }
                UiState.Succes -> {
                    binding.root.findNavController().navigate(R.id.action_signInFragment_to_scheduleFragment)
                }
                UiState.Wait -> {
                    val autoCompleteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, mainViewModel.autoCompleteData.value!!.map { it.name })
                    binding.accountLinkDataTv.setAdapter(autoCompleteAdapter)
                }
                UiState.Unathorized -> {}
                UiState.DataError -> {}
                UiState.NetworkError -> {
                    Snackbar.make(binding.root,"Ошибка подключения к сети",Snackbar.LENGTH_LONG).setAnchorView(binding.signInButton).show()
                    mainViewModel.waitState()
                }

                UiState.PreSucces -> if (isStudent)
                    mainViewModel.associateGroup(linkData)
                else
                    mainViewModel.associateMentor(linkData)
            }
        })
    }

}