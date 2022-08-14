package tech.siham.papp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tech.siham.papp.MainActivity
import tech.siham.papp.R
import tech.siham.papp.data.requests.LoginRequest
import tech.siham.papp.data.requests.RegisterRequest
import tech.siham.papp.databinding.ActivityLoginBinding
import tech.siham.papp.databinding.ActivityRegisterBinding
import tech.siham.papp.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginRegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(LoginRegisterViewModel::class.java)


        binding.lifecycleOwner = this

        binding.data = loginViewModel

        val messageView: TextView = binding.textMessage

        binding.goRegisterActivity.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        loginViewModel.loginDetails.observe(this, Observer {
            messageView.text = it.toString()
            if(it.toString() == "user login successfully"){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        binding.buttonLogin.setOnClickListener{
            // val email = binding.email.text.toString().trim()
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            when{
                username.isEmpty() -> {
                    messageView.text = "* please enter username"
                }
                password.isEmpty() -> {
                    messageView.text = "* please enter password"
                }

                else -> {
                    SessionManager().logoutAuthToken()
                    SessionManager().clearUserId()

                    loginViewModel.getLogin(
                        LoginRequest(username, password)
                    )

                }
            }
        }

    }
}