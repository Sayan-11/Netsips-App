package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentArchiveBinding
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

class ArchiveFragment : Fragment() {

    private var mDb: AppDatabase? = null

    private lateinit var binding: FragmentArchiveBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_archive, container, false)

        mDb = AppDatabase.getInstance(requireContext())

        val articles = mDb?.articlesDao()?.getArchivedArticles()
        if (articles != null && articles.isNotEmpty()) {
            binding.archiveRecycler.layoutManager = LinearLayoutManager(context)
            binding.archiveRecycler.adapter = ArticleAdapter(articles)
        } else {
            Log.d("ArchiveFragment", "No articles")
        }


        return binding.root
    }


}
