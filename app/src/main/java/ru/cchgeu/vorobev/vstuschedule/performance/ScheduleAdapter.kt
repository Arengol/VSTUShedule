package ru.cchgeu.vorobev.vstuschedule.performance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.cchgeu.vorobev.vstuschedule.R
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.MainViewModel

class ScheduleAdapter (private val viewModel: MainViewModel):
    RecyclerView.Adapter<ScheduleAdapter.ItemViewHolder>(){
        lateinit var context: Context

        class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val timeTextView : TextView = itemView.findViewById(R.id.time)
            val classNameTextView : TextView = itemView.findViewById(R.id.ClassName)
            val auditoryTextView : TextView = itemView.findViewById(R.id.Auditory)
            val mentorTextView : TextView = itemView.findViewById(R.id.Mentor)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        context = parent.context
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return viewModel.schedule.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val time = viewModel.schedule.value?.get(position)?.time?.replace("""-""".toRegex(), "\n") ?: ""
        val name = viewModel.schedule.value?.get(position)?.name ?: ""
        var auditory = ""
        viewModel.schedule.value?.get(position)?.auditory?.forEach { auditory+="$it\n" }
        var mentor = ""
        if (viewModel.schedule.value?.get(position)?.mentor != null)
        viewModel.schedule.value?.get(position)?.mentor?.forEach { mentor +="$it\n" }
        else
            mentor = viewModel.schedule.value?.get(position)?.group ?: ""
        holder.auditoryTextView.text = auditory
        holder.timeTextView.text = time
        holder.classNameTextView.text = name
        holder.mentorTextView.text = mentor
    }
}