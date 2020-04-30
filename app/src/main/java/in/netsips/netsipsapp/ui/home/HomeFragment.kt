package `in`.netsips.netsipsapp.ui.home

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.FragmentHomeBinding
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

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.currentSessionRecycler.layoutManager = LinearLayoutManager(context)

        val viewModel =
            ViewModelProvider(this, HomeViewModelFactory(requireActivity().application)).get(
                HomeViewModel::class.java
            )
        viewModel.currentSessionArticles!!.observe(viewLifecycleOwner, Observer { articles ->
            if (articles.isNotEmpty()) {
                binding.currentSessionRecycler.visibility = View.VISIBLE
                binding.currentSessionRecycler.adapter = ArticleAdapter(articles)
                binding.noArticlesEmpty.visibility = View.GONE
            }
        })

        return binding.root
    }
}


