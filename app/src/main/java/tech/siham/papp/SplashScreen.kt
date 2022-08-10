package tech.siham.papp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import tech.siham.papp.ui.auth.register.RegisterActivity
import tech.siham.papp.utils.SessionManager


@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            val intentRegister = Intent(this, RegisterActivity::class.java)

            startActivity(if(SessionManager().fetchAuthToken() == null) intentRegister else intent )

            finish()
        }, 2000) // 2000 is the delayed time in milliseconds.
    }
}