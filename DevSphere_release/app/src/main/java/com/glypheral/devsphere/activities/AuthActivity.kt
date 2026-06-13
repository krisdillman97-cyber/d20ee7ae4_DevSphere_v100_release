package com.glypheral.devsphere.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.glypheral.devsphere.R
import com.glypheral.devsphere.models.User
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager
import com.glypheral.devsphere.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.util.UUID

class AuthActivity : AppCompatActivity() {
    companion object {
        private const val RC_GOOGLE = 9001
        private const val TAG = "AuthActivity"
    }

    private var isSignUp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setupViews()
    }

    private fun setupViews() {
        val btnSignIn    = findViewById<Button>(R.id.btnSignIn)
        val btnGoogle    = findViewById<Button>(R.id.btnGoogle)
        val btnGitHub    = findViewById<Button>(R.id.btnGitHub)
        val btnToggle    = findViewById<TextView>(R.id.btnToggleMode)
        val etName       = findViewById<EditText>(R.id.etName)
        val etEmail      = findViewById<EditText>(R.id.etEmail)
        val etPassword   = findViewById<EditText>(R.id.etPassword)
        val tvTitle      = findViewById<TextView>(R.id.tvAuthTitle)
        val nameLayout   = findViewById<View>(R.id.nameLayout)

        fun refresh() {
            tvTitle.text = if (isSignUp) "Create Account" else "Sign In"
            btnSignIn.text = if (isSignUp) "Create Account" else "Sign In"
            btnToggle.text = if (isSignUp) "Already have an account? Sign In" else "New to DevSphere? Sign Up"
            nameLayout.visibility = if (isSignUp) View.VISIBLE else View.GONE
        }
        refresh()

        btnToggle.setOnClickListener { isSignUp = !isSignUp; refresh() }

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass  = etPassword.text.toString()
            val name  = etName.text.toString().trim()
            if (email.isEmpty() || pass.isEmpty()) { toast("Fill all fields"); return@setOnClickListener }
            handleEmailAuth(email, pass, if (isSignUp) name else null)
        }

        btnGoogle.setOnClickListener { launchGoogleSignIn() }

        btnGitHub.setOnClickListener {
            // GitHub OAuth — open browser to OAuth flow
            val url = "https://github.com/login/oauth/authorize?client_id=YOUR_CLIENT_ID&scope=user:email"
            val i = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
            startActivity(i)
        }
    }

    private fun handleEmailAuth(email: String, pass: String, name: String?) {
        // Simulated — integrate with your backend / Firebase Auth
        val user = User(
            id = UUID.randomUUID().toString(),
            name = name ?: email.substringBefore("@"),
            email = email,
            role = UserRole.FREE
        )
        SessionManager.saveSession(user, "token_${user.id}")
        navigateToMain()
    }

    private fun launchGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()
        val client = GoogleSignIn.getClient(this, gso)
        startActivityForResult(client.signInIntent, RC_GOOGLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)
                val user = User(
                    id = account.id ?: UUID.randomUUID().toString(),
                    name = account.displayName ?: "",
                    email = account.email ?: "",
                    role = UserRole.FREE,
                    googleLinked = true
                )
                SessionManager.saveSession(user, "google_${user.id}")
                navigateToMain()
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign-in failed: ${e.statusCode}")
                toast("Google sign-in failed")
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
