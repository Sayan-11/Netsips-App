package `in`.netsips.netsipsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsViewModel : ViewModel() {

    private val database =
        FirebaseDatabase.getInstance().reference.child(FirebaseAuth.getInstance().currentUser!!.uid)

    var daysSelected: MutableLiveData<String> = MutableLiveData()

    private val daysListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val days = dataSnapshot.child("days").value.toString()
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
        database.child("days").setValue(days)
    }

}