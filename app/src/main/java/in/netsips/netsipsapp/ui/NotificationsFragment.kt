package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class NotificationsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }



}
