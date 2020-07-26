package `in`.netsips.netsipsapp.ui.viewmodel

import `in`.netsips.netsipsapp.R
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class SettingsViewModel(private val application: Application) : ViewModel() {

    private val database =
        FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().currentUser!!.uid)

    var daysSelected: MutableLiveData<String> = MutableLiveData()

    private val daysListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val days =
                dataSnapshot.child(application.getString(R.string.rtd_field_days)).value.toString()
            daysSelected.value = days
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("SettingsFragment", "Read failed")
        }
    }

    init {
        database.addValueEventListener(daysListener)
    }

    fun updateDays(days: String) {
        database.child(application.getString(R.string.rtd_field_days)).setValue(days)
    }
    var name : String? =
        if(FirebaseAuth.getInstance().currentUser?.displayName==null||FirebaseAuth.getInstance().currentUser?.displayName=="")
            GoogleSignIn.getLastSignedInAccount(application)?.displayName.toString()
                .toLowerCase(Locale.ROOT).capitalize()
        else
            FirebaseAuth.getInstance().currentUser?.displayName.toString()
                .toLowerCase(Locale.ROOT).capitalize()
    var email : String? =
        if(FirebaseAuth.getInstance().currentUser?.email==null||FirebaseAuth.getInstance().currentUser?.email=="")
            GoogleSignIn.getLastSignedInAccount(application)?.email
        else
            FirebaseAuth.getInstance().currentUser?.email

    var photo= GoogleSignIn.getLastSignedInAccount(application)?.photoUrl?: FirebaseAuth.getInstance().currentUser?.photoUrl

}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(application) as T
    }
}