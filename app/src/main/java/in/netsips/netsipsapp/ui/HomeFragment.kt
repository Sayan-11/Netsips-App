package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentHomeBinding
import `in`.netsips.netsipsapp.helper.AppDatabase
import `in`.netsips.netsipsapp.helper.ArticleAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "current_session")
            .allowMainThreadQueries().build()

        val articlesList = db.userDao().getAllArticles()

        binding.currentSessionRecycler.layoutManager = LinearLayoutManager(context)
        binding.currentSessionRecycler.adapter = ArticleAdapter(articlesList)

        return binding.root
    }

}
