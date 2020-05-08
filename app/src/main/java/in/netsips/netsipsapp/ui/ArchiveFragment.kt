package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentArchiveBinding
import `in`.netsips.netsipsapp.helper.ArticleAdapter
import `in`.netsips.netsipsapp.helper.SwipeToDeleteCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteArticle(viewModel.allArticles.value!![viewHolder.adapterPosition].docID)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.archiveRecycler)

        viewModel.getArchivedArticles().observe(viewLifecycleOwner, Observer { articles ->
            if (!articles.isNullOrEmpty()) {
                val adapter = ArticleAdapter(articles)
                binding.archiveRecycler.visibility = View.VISIBLE
                binding.archiveRecycler.adapter = adapter
                binding.noArticlesEmpty.visibility = View.GONE
            } else {
                binding.archiveRecycler.visibility = View.INVISIBLE
                binding.noArticlesEmpty.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

}
