package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.LoginActivity
import `in`.netsips.netsipsapp.LoginActivity.Companion.CLIENT_ID
import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentSettingsBinding
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        //val sharedPref = activity?.getSharedPreferences("NightMode", Context.MODE_PRIVATE)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return null

        var Night:Boolean=sharedPref.getBoolean("NightMode",false)
//        with (sharedPref.edit()) {
//            putBoolean("NightMode", Night)
//            commit()
//        }
        if(Night){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switch1.isChecked=true

        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switch1.isChecked=false

        }

//        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//        when (isNightTheme) {
//            Configuration.UI_MODE_NIGHT_YES ->{
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                binding.switch1.isChecked=true}
//            Configuration.UI_MODE_NIGHT_NO ->{
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                binding.switch1.isChecked=false}
//        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == binding.radioButton3.id) {
                binding.customWeekdays.visibility=View.VISIBLE
            }
            else
                binding.customWeekdays.visibility=View.GONE

        }

        binding.switch1.setOnCheckedChangeListener{_,_ ->
            if(binding.switch1.isChecked){
                Night=true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                with (sharedPref.edit()) {
                    putBoolean("NightMode", Night)
                    commit()
                }
            }
            else{
                Night=false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                with (sharedPref.edit()) {
                    putBoolean("NightMode", Night)
                    commit()
                }
            }
        }
//        val checkBoxes = arrayListOf(
//            binding.dayMondayCheckbox,
//            binding.dayTuesdayCheckbox,
//            binding.dayWednesdayCheckbox,
//            binding.dayThursdayCheckbox,
//            binding.dayFridayCheckbox,
//            binding.daySaturdayCheckbox,
//            binding.daySundayCheckbox
//        )
//
//        val viewModel =
//            ViewModelProvider(this, SettingsViewModelaFactory(requireActivity().application)).get(
//                SettingsViewModel::class.java
//            )
//        viewModel.daysSelected.observe(viewLifecycleOwner, Observer { days ->
//            checkBoxes.forEach { it.isChecked = false }
//            for (dayChar in days) {
//                val day = Character.getNumericValue(dayChar)
//                if (day in 0..6)
//                    checkBoxes[day].isChecked = true
//            }
//        })
//
//        binding.saveButton.setOnClickListener {
//            var days = ""
//            checkBoxes.forEach {
//                if (it.isChecked)
//                    days += when (it.id) {
//                        R.id.day_monday_checkbox -> "0 "
//                        R.id.day_tuesday_checkbox -> "1 "
//                        R.id.day_wednesday_checkbox -> "2 "
//                        R.id.day_thursday_checkbox -> "3 "
//                        R.id.day_friday_checkbox -> "4 "
//                        R.id.day_saturday_checkbox -> "5 "
//                        R.id.day_sunday_checkbox -> "6 "
//                        else -> ""
//                    }
//            }
//            viewModel.updateDays(days)
//        }

        binding.LogOut.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.sign_out_title)
                .setMessage(R.string.sign_out_message)
                .setPositiveButton(R.string.sign_out_positive) { dialog, which ->
                    FirebaseAuth.getInstance().signOut()

                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(CLIENT_ID)
                        .requestEmail()
                        .build()
                    GoogleSignIn.getClient(requireContext(), gso).signOut()

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
