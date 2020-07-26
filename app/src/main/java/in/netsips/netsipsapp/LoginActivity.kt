package `in`.netsips.netsipsapp

import `in`.netsips.netsipsapp.databinding.ActivityLoginBinding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {

    private val signInRequestCode = 1001
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        const val CLIENT_ID =
            "617103322863-h8lcrfkb4jauaq1q8mpm3etncebdsbjg.apps.googleusercontent.com"
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

        binding.button.setOnClickListener {
            val mail = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()
            if (mail.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(mail.trim(), pass.trim())
                    .addOnSuccessListener { startActivity(Intent(this, MainActivity::class.java)) }
                    .addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Authentication Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
        }
        binding.textView.setOnClickListener {
            val mail = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword.text.toString()
            if (mail.isNotEmpty() && pass.isNotEmpty()) {
                auth.sendPasswordResetEmail(mail.trim())
                    .addOnSuccessListener { Toast.makeText(this, "Reset Email Sent", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this, "Not a Registered Email", Toast.LENGTH_SHORT).show()  }
            }
            else{
                Toast.makeText(this, "Please enter an Email Address first", Toast.LENGTH_SHORT).show()
            }

        }
        binding.textView2.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }
        binding.googleSignin.setOnClickListener {
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
//                    rtd.child(getString(R.string.rtd_field_days))
//                        .setValue("${getString(R.string.rtd_field_days_default_value)} ")
                    rtd.child(getString(R.string.rtd_field_email)).setValue(user.email)
                    Log.e("Days",user.uid)
                    rtd.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.hasChild(getString(R.string.rtd_field_days))) {

                                    //User Exists , No Need To add new data.
                                    //Get previous data from firebase. It will take previous data as soon as possible..
                                    return
                                } else {
                                    rtd.child(getString(R.string.rtd_field_days)).setValue("${getString(R.string.rtd_field_days_default_value)} ")
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
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
