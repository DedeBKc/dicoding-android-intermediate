package com.dedebkc.intermediate.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.dedebkc.intermediate.R
import com.dedebkc.intermediate.ui.ViewModelFactory
import com.dedebkc.intermediate.ui.mainmenu.MainActivity

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var factory: ViewModelFactory
    private val authenticationViewModel: AuthenticationViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        factory = ViewModelFactory.getInstance(this)

        val delayMillis: Long = 2000
        setupView()

        authenticationViewModel.getUserToken().observe(this) { token ->
            if (token.isNullOrEmpty() || token == "not_set_yet") {
                Handler(Looper.getMainLooper()).postDelayed({
                    supportFragmentManager.commit {
                        replace(R.id.placeholder, LoginFragment(), LoginFragment::class.java.simpleName)
                    }
                }, delayMillis)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, delayMillis)
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}