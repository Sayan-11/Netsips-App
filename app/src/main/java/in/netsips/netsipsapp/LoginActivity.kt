package `in`.netsips.netsipsapp

import `in`.netsips.netsipsapp.databinding.ActivityLoginBinding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {

    private val signInRequestCode = 1001

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        const val CLIENT_ID =
            "617103322863-30iggerpekrhag1ga91q90joc5ro3tc0.apps.googleusercontent.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

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
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
                Snackbar.make(
                    binding.loginCoordinator,
                    getString(R.string.sign_in_failed),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val rtd = FirebaseDatabase.getInstance().reference.child(user!!.uid)
                    rtd.child(getString(R.string.rtd_field_days))
                        .setValue("${getString(R.string.rtd_field_days_default_value)} ")
                    rtd.child(getString(R.string.rtd_field_email)).setValue(user.email)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    task.exception?.printStackTrace()
                    Log.d("LoginActivity", task.exception.toString())
                    Snackbar.make(
                        binding.loginCoordinator,
                        getString(R.string.sign_in_failed),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

}
