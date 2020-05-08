package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.LoginActivity
import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentSettingsBinding
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.signoutFab.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.sign_out_title)
                .setMessage(R.string.sign_out_message)
                .setPositiveButton(R.string.sign_out_positive) { dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNeutralButton(R.string.sign_out_neutral) { dialog, which -> //Do nothing
                }
                .show()
        }

        return binding.root
    }
}
