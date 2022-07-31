package tech.siham.papp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.data.requests.RegisterRequest
import tech.siham.papp.databinding.FragmentRegisterBinding
import tech.siham.papp.utils.SessionManager

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registerViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(RegisterViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.lifecycleOwner = this

        // binding.data = registerViewModel

        val messageView: TextView = binding.textMessage

        val username: EditText = binding.username
        val password: EditText = binding.password
        val email: EditText = binding.email

//        loginViewModel.details.observe(viewLifecycleOwner, Observer {
//            messageView.text = it.toString()
//        })

        registerViewModel.details.observe(viewLifecycleOwner, Observer {
            messageView.text = it.toString()
        })

        binding.buttonRegister.setOnClickListener {
            SessionManager().logoutAuthToken()
            registerViewModel.setRegister(RegisterRequest(username.text.toString(), email.text.toString(), password.text.toString()))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}