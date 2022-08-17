package tech.siham.papp.ui.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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


        binding.post.adapter = PostListAdapter(
            PostListAdapter.OnClickListener{
                Toast.makeText(requireContext(), "liked: ${it.id.toString()}" , Toast.LENGTH_LONG).show()
                postViewModel.setLike(it.id)
            }
        )

        postViewModel.details.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "details like: $it." , Toast.LENGTH_LONG).show()
            Log.i("From post Fragment: ", "details : $it")

        })

//        binding.post.adapter?.notifyDataSetChanged()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}