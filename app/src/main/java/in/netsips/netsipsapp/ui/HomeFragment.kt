package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentHomeBinding
import `in`.netsips.netsipsapp.helper.AppDatabase
import `in`.netsips.netsipsapp.helper.ArticleAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

class HomeFragment : Fragment() {

    private var mDb: AppDatabase? = null

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        mDb = AppDatabase.getInstance(requireContext())

        val articles = mDb?.articlesDao()?.getCurrentSessionArticles()
        if (articles != null && articles.isNotEmpty()) {
            binding.currentSessionRecycler.layoutManager = LinearLayoutManager(context)
            binding.currentSessionRecycler.adapter = ArticleAdapter(articles)
        } else {
            Log.d("HomeFragment", "No articles")
        }

        return binding.root
    }
}


