package com.example.nytimesapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytimesapp.R
import com.example.nytimesapp.other.Status
import com.example.nytimesapp.ui.adapters.ArticlesAdapter
import com.example.nytimesapp.viewmodels.ArticlesViewModel

import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_articles.*

import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_articles.rootLayout as rootLayout1

@AndroidEntryPoint
class ArticlesFragment @Inject constructor(
    val articlesAdapter: ArticlesAdapter,
    var viewModel: ArticlesViewModel? = null
) : Fragment(R.layout.fragment_articles) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            viewModel ?: ViewModelProvider(requireActivity()).get(ArticlesViewModel::class.java)
        subscribeToObservers()
        setupRecyclerView()
    }

    /**
     *
     */
    private fun navigateToNytFragment() {
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.action_nytFragment_to_articlesFragment)
    }

    /**
     *
     */
    private fun subscribeToObservers() {
        viewModel?.popularArticles?.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        articlesAdapter.submitList(result.data?.articles ?: listOf())
                        progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            result.message ?: "An unknown error occurred.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    /**
     *
     */
    private fun setupRecyclerView() {
        rvArticles.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
















