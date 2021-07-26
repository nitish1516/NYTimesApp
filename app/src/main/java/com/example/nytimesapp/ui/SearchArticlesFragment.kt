package com.example.nytimesapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytimesapp.R
import com.example.nytimesapp.other.Constants.SEARCH_TIME_DELAY
import com.example.nytimesapp.other.Status
import com.example.nytimesapp.ui.adapters.SearchArticlesAdapter
import com.example.nytimesapp.viewmodels.ArticlesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search_article.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchArticlesFragment @Inject constructor(
    val searchArticlesAdapter: SearchArticlesAdapter
) : Fragment(R.layout.fragment_search_article) {

    lateinit var viewModel: ArticlesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ArticlesViewModel::class.java)
        setupRecyclerView()
        subscribeToObservers()

        var job: Job? = null
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(SEARCH_TIME_DELAY)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            viewModel.searchForArticles(editable.toString())
                        }
                    }
                }
            }

        })





        searchArticlesAdapter.setOnItemClickListener {
            findNavController().popBackStack()
        }
    }

    private fun subscribeToObservers() {
        viewModel.searchedArticles.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val articles = result.data?.response?.docs
                        searchArticlesAdapter.articles = articles ?: listOf()
                        progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().rootLayout,
                            result.message ?: "An unknown error occured.",
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

    private fun setupRecyclerView() {
        rvSearchedArticles.apply {
            adapter = searchArticlesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}