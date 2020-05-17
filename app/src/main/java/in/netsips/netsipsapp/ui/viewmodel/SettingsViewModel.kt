package `in`.netsips.netsipsapp.ui.viewmodel

import `in`.netsips.netsipsapp.R
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(application) as T
    }
}