package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentArchiveBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment


class ArchiveFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentArchiveBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_archive, container, false)


        return binding.root
    }

}
