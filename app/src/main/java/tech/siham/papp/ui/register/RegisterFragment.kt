package tech.siham.papp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.data.requests.RegisterRequest
import tech.siham.papp.databinding.FragmentRegisterBinding
import tech.siham.papp.ui.auth.LoginRegisterViewModel
import tech.siham.papp.utils.SessionManager

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: LoginRegisterViewModel
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
            ).get(LoginRegisterViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.lifecycleOwner = this

        // binding.data = registerViewModel

        val messageView: TextView = binding.textMessage

        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val email = binding.email.text.toString().trim()

        registerViewModel.registerDetails.observe(viewLifecycleOwner, Observer {
            messageView.text = it.toString()
        })

        binding.buttonRegister.setOnClickListener {

            when{
                username.isEmpty() -> {
                    messageView.text = "please enter username"
                }
                email.isEmpty() -> {
                    messageView.text = "please enter email"
                }
                password.isEmpty() -> {
                    messageView.text = "please enter password"
                }
                else -> {
                    SessionManager().logoutAuthToken()
                    SessionManager().clearUserId()

                    registerViewModel.setRegister(
                        RegisterRequest(
                            username,
                            email,
                            password
                        )
                    )
                }
            }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}