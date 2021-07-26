package com.example.nytimesapp.data.repositories

import com.example.nytimesapp.data.vo.ArticleSearchResponse
import com.example.nytimesapp.data.vo.PopularArticlesResponse
import com.example.nytimesapp.other.Resource


interface ArticlesRepository {

    suspend fun getPopularArticle(type: String): Resource<PopularArticlesResponse>

    suspend fun searchForArticle(query: String): Resource<ArticleSearchResponse>
}