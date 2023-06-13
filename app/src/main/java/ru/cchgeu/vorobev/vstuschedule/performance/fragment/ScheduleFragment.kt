package ru.cchgeu.vorobev.vstuschedule.performance.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.cchgeu.vorobev.vstuschedule.R
import ru.cchgeu.vorobev.vstuschedule.databinding.FragmentScheduleBinding
import ru.cchgeu.vorobev.vstuschedule.performance.ScheduleAdapter
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.MainViewModel
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.UiState
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragment : Fragment() {
    @Inject
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel.getMainSchedule()
        mainViewModel.getAllGroups()
        mainViewModel.getAllMentors()
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener {
            mainViewModel.nextSchedule()
        }
        binding.previousButton.setOnClickListener {
            mainViewModel.previousSchedule()
        }
        binding.closeButton.setOnClickListener {
            mainViewModel.getMainSchedule()
            binding.searchButton.visibility = View.VISIBLE
            binding.closeButton.visibility = INVISIBLE
        }
        binding.searchButton.setOnClickListener {
            if (binding.searchTv.text.isEmpty())
                Snackbar.make(binding.root,"Поле не может быть пустым", Snackbar.LENGTH_LONG).show()
            else {
            mainViewModel.getSchedule(
                binding.searchTv.text.toString()
            )
                binding.searchButton.visibility = INVISIBLE
                binding.closeButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.state.observe(this) {
            when(it){
                UiState.DataError -> {}
                UiState.InvalidLoginOrPass -> {}
                UiState.Loading -> {}
                UiState.NetworkError -> {}
                UiState.PreSucces -> {}
                UiState.Unathorized -> {}
                UiState.Wait -> {
                    binding.searchTv.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_expandable_list_item_1,
                            mainViewModel.autoCompleteData.value!!.map { it.name }
                        )
                    )
                }
                UiState.Succes -> {
                    binding.recyclerView.adapter = ScheduleAdapter(mainViewModel)
                    binding.dayOfWeekInfo.text = mainViewModel.weekTextView
                }
            }
        }
    }
}