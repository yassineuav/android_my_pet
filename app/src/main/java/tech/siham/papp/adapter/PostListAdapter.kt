package tech.siham.papp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tech.siham.papp.databinding.ListPostsItemsBinding
import tech.siham.papp.models.Post


class PostListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Post, PostListAdapter.PostViewHolder>(DiffCallback){

    companion object DiffCallback: ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class PostViewHolder(private var binding: ListPostsItemsBinding): RecyclerView.ViewHolder(binding.root){
        val likeButton = binding.likeButton

        fun bind(post: Post){
            binding.post = post
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(ListPostsItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)

        holder.likeButton.setOnClickListener{
            onClickListener.onClick(post)
        }
        holder.bind(post)
    }

    class OnClickListener(val clickListener: (post: Post) -> Unit){
        fun onClick(post: Post) = clickListener(post)
    }

}