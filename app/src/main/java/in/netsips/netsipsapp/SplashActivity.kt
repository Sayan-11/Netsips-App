package `in`.netsips.netsipsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val signedInAccount = auth.currentUser
        if(signedInAccount!=null) {
            //User has signed in before. Redirecting to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else {
            //User hasn't signed in before. Redirecting to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
