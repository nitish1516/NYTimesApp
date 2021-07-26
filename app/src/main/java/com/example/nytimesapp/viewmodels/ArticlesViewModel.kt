package com.example.nytimesapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nytimesapp.data.repositories.ArticlesRepository
import com.example.nytimesapp.data.vo.ArticleSearchResponse
import com.example.nytimesapp.data.vo.PopularArticlesResponse
import com.example.nytimesapp.other.Event
import com.example.nytimesapp.other.Resource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repository: ArticlesRepository
) : ViewModel() {

    /**
     * Livedata to hold searched articles
     */
    val _searchedArticles = MutableLiveData<Event<Resource<ArticleSearchResponse>>>()
    val searchedArticles: LiveData<Event<Resource<ArticleSearchResponse>>> = _searchedArticles


    /**
     * Livedata to hold popular articles
     */
    private val _popularArticles = MutableLiveData<Event<Resource<PopularArticlesResponse>>>()
    val popularArticles: LiveData<Event<Resource<PopularArticlesResponse>>> = _popularArticles

    fun addPopularArticles(response: Resource<PopularArticlesResponse>) {
        _popularArticles.value = Event(response)
    }

    /**
     * Get Popular articles from server
     */
    fun getPopularArticles(type: String) {
        if (type.isEmpty()) return
        _popularArticles.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getPopularArticle(type)
            addPopularArticles(response)
        }
    }


    /**
     * Search articles from server
     */
    fun searchForArticles(imageQuery: String) {
        if (imageQuery.isEmpty()) return
        _searchedArticles.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.searchForArticle(imageQuery)
            _searchedArticles.value = Event(response)
        }
    }
}













