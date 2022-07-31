package tech.siham.papp.ui.home

import androidx.lifecycle.LifecycleOwner
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.adapter.InterestedInListAdapter
import tech.siham.papp.databinding.FragmentInterestedInBinding

class HomeFragment : Fragment(), LifecycleOwner {

    private lateinit var homeViewModel: HomeViewModel

    private var _binding: FragmentInterestedInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HomeViewModel::class.java)


        _binding = FragmentInterestedInBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.data = homeViewModel

        binding.interestedIn.adapter = InterestedInListAdapter(InterestedInListAdapter.OnClickListener{
            homeViewModel.displayInterestedInDetails(it)
        })

        homeViewModel.navigateToSelectedInterestedIn.observe(viewLifecycleOwner, Observer {
            if( null != it){
               // this.findNavController().navigate(HomeFragmentDirections.ActionNavHomeToDetailFragment(it))
               // homeViewModel.displayInterestedInComplete()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}