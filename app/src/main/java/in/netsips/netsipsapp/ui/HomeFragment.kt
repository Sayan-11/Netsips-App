package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentHomeBinding
import `in`.netsips.netsipsapp.helper.ArticleAdapter
import `in`.netsips.netsipsapp.helper.SwipeToDeleteShareCallback
import `in`.netsips.netsipsapp.ui.viewmodel.FirestoreViewModel
import `in`.netsips.netsipsapp.ui.viewmodel.FirestoreViewModelFactory
import android.content.Intent
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
    lateinit var adapter:ArticleAdapter// for notifying app

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

        val swipeHandler = object : SwipeToDeleteShareCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT)
                    viewModel.deleteArticle(viewModel.currentArticles.value!![viewHolder.adapterPosition].docID)
                else{
                    val intent= Intent()
                    intent.action=Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TEXT,viewModel.currentArticles.value!![viewHolder.adapterPosition].articleURL)
                    intent.type="text/plain"
                    startActivity(Intent.createChooser(intent,"Share To:"))
                    adapter.notifyDataSetChanged()
                }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.currentSessionRecycler)



        viewModel.getSavedArticles().observe(viewLifecycleOwner, Observer { articles ->
            if (!articles.isNullOrEmpty()) {
                adapter = ArticleAdapter(articles)
                binding.currentSessionRecycler.visibility = View.VISIBLE
                binding.currentSessionRecycler.adapter = adapter
                binding.noArticlesEmpty.visibility = View.GONE
            } else {
                binding.currentSessionRecycler.visibility = View.INVISIBLE
                binding.noArticlesEmpty.visibility = View.VISIBLE
            }
        })

        return binding.root
    }
}


