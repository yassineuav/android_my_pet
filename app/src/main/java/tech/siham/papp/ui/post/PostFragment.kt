package tech.siham.papp.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.adapter.PostListAdapter
import tech.siham.papp.databinding.FragmentPostBinding


class PostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(PostViewModel::class.java)


        _binding = FragmentPostBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.data = postViewModel

        //binding.post.adapter?.notifyDataSetChanged()

        binding.post.adapter = PostListAdapter(
            PostListAdapter.OnClickListener{
//                showDeleteDialog(it)
                Toast.makeText(requireContext(), "clicked: "+ it.toString(), Toast.LENGTH_LONG).show()

            },
        )

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}