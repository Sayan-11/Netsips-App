package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentHomeBinding
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

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.currentSessionRecycler.layoutManager = LinearLayoutManager(context)

        val viewModel =
            ViewModelProvider(
                this,
                FirestoreViewModelFactory(
                    requireActivity().application
                )
            ).get(
                FirestoreViewModel::class.java
            )

        viewModel.getCurrentArticles().observe(viewLifecycleOwner, Observer { articles ->
            if (articles.isNotEmpty()) {
                val adapter = ArticleAdapter(articles)

                binding.currentSessionRecycler.visibility = View.VISIBLE
                binding.currentSessionRecycler.adapter = adapter
                binding.noArticlesEmpty.visibility = View.GONE

                val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        viewModel.deleteArticle(viewModel.currentArticles.value!![viewHolder.adapterPosition].docID)
                    }
                }
                ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.currentSessionRecycler)
            }
        })

        return binding.root
    }
}


