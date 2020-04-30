package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentNotificationsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)



        return binding.root
    }
}
