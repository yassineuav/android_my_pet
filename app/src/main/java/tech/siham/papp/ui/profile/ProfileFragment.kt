package tech.siham.papp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.databinding.FragmentProfileBinding
import tech.siham.papp.utils.SessionManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(ProfileViewModel::class.java)


        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // binding.lifecycleOwner = this
        // binding.data = loginViewModel

        val textView: TextView = binding.textProfile

        binding.getUser.setOnClickListener {
            profileViewModel.getUser()
        }

        binding.buttonLogout.setOnClickListener {
            SessionManager().logoutAuthToken()
            SessionManager().clearUserId()
            profileViewModel.getUser()
        }

        profileViewModel.user.observe(viewLifecycleOwner, Observer {
            textView.text = it.toString()
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}