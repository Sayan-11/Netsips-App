package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentArchiveBinding
import `in`.netsips.netsipsapp.helper.ArticleAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class ArchiveFragment : Fragment() {

    private lateinit var binding: FragmentArchiveBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_archive, container, false)

        binding.archiveRecycler.layoutManager = LinearLayoutManager(context)

        val viewModel =
            ViewModelProvider(
                this,
                FirestoreViewModelFactory(
                    requireActivity().application
                )
            ).get(
                FirestoreViewModel::class.java
            )
        viewModel.getAllArticles().observe(viewLifecycleOwner, Observer { articles ->
            if (articles.isNotEmpty()) {
                binding.archiveRecycler.visibility = View.VISIBLE
                binding.archiveRecycler.adapter = ArticleAdapter(articles)
                binding.noArticlesEmpty.visibility = View.GONE
            }
        })

        return binding.root
    }

}
