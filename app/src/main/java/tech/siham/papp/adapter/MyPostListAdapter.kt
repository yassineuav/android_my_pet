package tech.siham.papp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tech.siham.papp.databinding.PostItemsBinding
import tech.siham.papp.models.MyPost

class MyPostListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<MyPost, MyPostListAdapter.MyPostViewHolder>(DiffCallback){

    companion object DiffCallback: ItemCallback<MyPost>() {
        override fun areItemsTheSame(oldItem: MyPost, newItem: MyPost): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: MyPost, newItem: MyPost): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class MyPostViewHolder(private var binding: PostItemsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: MyPost){
            binding.myPost = post
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
        return MyPostViewHolder(PostItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
        val post = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(post)
        }
        holder.bind(post)
    }

    class OnClickListener(val clickListener: (post: MyPost) -> Unit){
        fun onClick(post: MyPost) = clickListener(post)
    }

}