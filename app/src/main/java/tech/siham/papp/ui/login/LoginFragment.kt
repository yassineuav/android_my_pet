package tech.siham.papp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.data.requests.LoginRequest
import tech.siham.papp.databinding.FragmentLoginBinding
import tech.siham.papp.utils.SessionManager

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loginViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.lifecycleOwner = this

        binding.data = loginViewModel

        val messageView: TextView = binding.textMessage

        val username: EditText = binding.username
        val password: EditText = binding.password

      loginViewModel.details.observe(viewLifecycleOwner, Observer {
          messageView.text = it.toString()
      })

//        loginViewModel.loginToken.observe(viewLifecycleOwner, Observer {
//            messageView.text = it.toString()
//        })

        binding.buttonLogin.setOnClickListener {
            SessionManager().logoutAuthToken()
            loginViewModel.getLogin(LoginRequest(username.text.toString(), password.text.toString()))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}