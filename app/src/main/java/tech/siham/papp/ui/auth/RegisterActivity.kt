package tech.siham.papp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.data.requests.RegisterRequest
import tech.siham.papp.databinding.ActivityRegisterBinding
import tech.siham.papp.utils.SessionManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(LoginRegisterViewModel::class.java)


        binding.lifecycleOwner = this

        binding.data = registerViewModel

        val messageView: TextView = binding.textMessage


        registerViewModel.registerErrorDetails.observe(this, Observer {
            messageView.text = it.toString()
        })

        binding.goLoginActivity.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.buttonRegister.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            when{
                username.isEmpty() -> {
                    messageView.text = "* please enter username"
                }
                email.isEmpty() -> {
                    messageView.text = "* please enter email"
                }
                password.isEmpty() -> {
                    messageView.text = "* please enter password"
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
    }

}