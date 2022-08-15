package tech.siham.papp.adapter

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import tech.siham.papp.R
import tech.siham.papp.models.InterestedIn
import tech.siham.papp.models.MyPost
import tech.siham.papp.models.Post
import tech.siham.papp.ui.auth.RegisterStatus
import tech.siham.papp.ui.home.interestedApiStatus
import tech.siham.papp.ui.login.loginStatus
import tech.siham.papp.ui.mypost.LoadingMyPostStatus
import tech.siham.papp.ui.post.loadingStatus


@BindingAdapter("listData")
fun bindRecycleView(recyclerView: RecyclerView, data: List<InterestedIn>?){
    val adapter = recyclerView.adapter as InterestedInListAdapter
    adapter.submitList(data)
}

@BindingAdapter("listPosts")
fun bindRecycleViewPost(recyclerView: RecyclerView, data: List<Post>?){
    val adapter = recyclerView.adapter as PostListAdapter
    adapter.submitList(data)
}

@BindingAdapter("myPosts")
fun bindRecycleViewMyPost(recyclerView: RecyclerView, data: List<MyPost>?){
    val adapter = recyclerView.adapter as MyPostListAdapter
    adapter.submitList(data)
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("http").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}


@BindingAdapter("interestedApiStatus")
fun bindStatus(statusImageView: ImageView, status: interestedApiStatus?){
    when(status){
        interestedApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        interestedApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        interestedApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {}
    }
}

@BindingAdapter("LoadingMyPostStatus")
fun bindStatus(statusImageView: ImageView, status: LoadingMyPostStatus?){
    when(status){
        LoadingMyPostStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        LoadingMyPostStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        LoadingMyPostStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        LoadingMyPostStatus.NO_ITEMS -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_add_reaction)
        }
        else -> {}
    }
}

@BindingAdapter("likedPost")
fun bindImage(statusImageView: ImageView, liked: Boolean?){
    if(liked == true){
        statusImageView.setImageResource(R.drawable.ic_baseline_favorite_24)
    }else{
        statusImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24)
    }

}


@BindingAdapter("loadingStatus")
fun bindStatus(statusImageView: ImageView, status: loadingStatus?){
    when(status){
        loadingStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        loadingStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        loadingStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        loadingStatus.NO_ITEMS -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_add_reaction)
        }
        else -> {}
    }
}

@BindingAdapter("loginStatus")
fun bindStatus(statusImageView: ImageView, status: loginStatus?){
    when(status){
        loginStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        loginStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        loginStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {}
    }
}

@BindingAdapter("RegisterStatus")
fun bindStatus(statusImageView: ImageView, status: RegisterStatus?){
    when(status){
        RegisterStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        RegisterStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        RegisterStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {}
    }
}