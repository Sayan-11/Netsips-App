package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.LoginActivity
import `in`.netsips.netsipsapp.LoginActivity.Companion.CLIENT_ID
import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.TrashActivity
import `in`.netsips.netsipsapp.databinding.FragmentSettingsBinding
import `in`.netsips.netsipsapp.ui.viewmodel.FirestoreViewModel
import `in`.netsips.netsipsapp.ui.viewmodel.FirestoreViewModelFactory
import `in`.netsips.netsipsapp.ui.viewmodel.SettingsViewModel
import `in`.netsips.netsipsapp.ui.viewmodel.SettingsViewModelFactory
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):View?{
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        //val sharedPref = activity?.getSharedPreferences("NightMode", Context.MODE_PRIVATE)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return null

        var night:Boolean=sharedPref.getBoolean("NightMode",false)
//        with (sharedPref.edit()) {
//            putBoolean("NightMode", Night)
//            commit()
//        }
        if(night){
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
            if(checkedId == binding.radioCustom.id) {
                binding.customWeekdays.visibility=View.VISIBLE
            }
            else
                binding.customWeekdays.visibility=View.GONE

        }

        binding.switch1.setOnCheckedChangeListener{_,_ ->
            if(binding.switch1.isChecked){
                night=true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                with (sharedPref.edit()) {
                    putBoolean("NightMode", night)
                    commit()
                }
            }
            else{
                night=false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                with (sharedPref.edit()) {
                    putBoolean("NightMode", night)
                    commit()
                }
            }
        }
        val radioButtons = arrayListOf(
            binding.radioDaily,
            binding.radioWeekly,
            binding.radioCustom
        )
        val checkBoxes = arrayListOf(
            binding.dayMondayCheckbox,
            binding.dayTuesdayCheckbox,
            binding.dayWednesdayCheckbox,
            binding.dayThursdayCheckbox,
            binding.dayFridayCheckbox,
            binding.daySaturdayCheckbox,
            binding.daySundayCheckbox
        )
        var viewModel1 =
            ViewModelProvider(
                this,
                FirestoreViewModelFactory(
                    requireActivity().application
                )
            ).get(
                FirestoreViewModel::class.java
            )
        viewModel1.getSavedArticles().observe(viewLifecycleOwner, Observer {
            binding.tvBookmarks.text="${it.size} Bookmarks Added"
        })

        val viewModel =
            ViewModelProvider(this, SettingsViewModelFactory(requireActivity().application)).get(
                SettingsViewModel::class.java
            )

        binding.tvName.text=viewModel.name
        binding.tvEmail.text = viewModel.email?.toLowerCase(Locale.ROOT)

        val imageUrl=viewModel.photo
        Picasso.with(context).load(imageUrl).resize(1000, 1000)
                .centerCrop()
                .placeholder(R.drawable.ic_profile_black).into(binding.roundedImageView)

        //setting radio and checkboxes
        viewModel.daysSelected.observe(viewLifecycleOwner, Observer { days ->
            checkBoxes.forEach { it.isChecked = false }

            val total: MutableList<Int> = mutableListOf()
            var i=0
            for (dayChar in days) {
                val day = Character.getNumericValue(dayChar)
                if(day!=-1){
                    total.add(day)
                    Log.e("total", total[i].toString())
                    i++
                }
            }
            Log.e("total", total.size.toString())
            if (total.size == 7){
                radioButtons[0].isChecked = true
            }
            else {
                if (total.size == 1&&total[0] == 6)
                    radioButtons[1].isChecked = true
                else {
                    radioButtons[2].isChecked = true
                    for (dayChar in days) {
                        val day = Character.getNumericValue(dayChar)
                        if(day!=-1)
                            checkBoxes[day].isChecked = true
                    }
                }
            }

        })

        binding.saveButton.setOnClickListener {
            var days = ""
            if(radioButtons[0].isChecked)
                days="0 1 2 3 4 5 6 "
            else if(radioButtons[1].isChecked)
                days="6 "
            else {
                checkBoxes.forEach {
                    if (it.isChecked)
                        days += when (it.id) {
                            R.id.day_monday_checkbox -> "0 "
                            R.id.day_tuesday_checkbox -> "1 "
                            R.id.day_wednesday_checkbox -> "2 "
                            R.id.day_thursday_checkbox -> "3 "
                            R.id.day_friday_checkbox -> "4 "
                            R.id.day_saturday_checkbox -> "5 "
                            R.id.day_sunday_checkbox -> "6 "
                            else -> ""
                        }
                }
            }
            viewModel.updateDays(days)
        }

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
        binding.trash.setOnClickListener{
            startActivity(Intent(context,TrashActivity::class.java))
        }

        return binding.root
    }


}
