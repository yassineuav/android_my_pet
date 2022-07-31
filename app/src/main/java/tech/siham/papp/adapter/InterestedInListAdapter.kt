package tech.siham.papp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tech.siham.papp.databinding.InterestedInItemsBinding
import tech.siham.papp.models.InterestedIn


class InterestedInListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<InterestedIn, InterestedInListAdapter.InterestedInViewHolder>(DiffCallback){

    companion object DiffCallback: ItemCallback<InterestedIn>() {
        override fun areItemsTheSame(oldItem: InterestedIn, newItem: InterestedIn): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: InterestedIn, newItem: InterestedIn): Boolean {
            return oldItem.url == newItem.url
        }
    }

    class InterestedInViewHolder(private var binding: InterestedInItemsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(interestedIn: InterestedIn){
            binding.interestedIn = interestedIn
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestedInViewHolder {
        return InterestedInViewHolder(InterestedInItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: InterestedInViewHolder, position: Int) {
        val interestedIn = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(interestedIn)
        }
        holder.bind(interestedIn)
    }

    class OnClickListener(val clickListener: (interestedIn: InterestedIn) -> Unit){
        fun onClick(interestedIn: InterestedIn) = clickListener(interestedIn)
    }

}