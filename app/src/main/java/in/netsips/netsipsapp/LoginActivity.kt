package `in`.netsips.netsipsapp

import `in`.netsips.netsipsapp.databinding.ActivityLoginBinding
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar


class LoginActivity : AppCompatActivity() {

    private val signInRequestCode = 1001

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.loginButton.setOnClickListener {
            loginWithGoogle()
        }
    }

    private fun loginWithGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, signInRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == signInRequestCode) {
            if (resultCode == 0) {
                Snackbar.make(
                    binding.loginCoordinator,
                    getString(R.string.sign_in_failed),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                //User has now signed in. Handle the sign in.
                handleSignIn()
            }
        }
    }

    private fun handleSignIn() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
