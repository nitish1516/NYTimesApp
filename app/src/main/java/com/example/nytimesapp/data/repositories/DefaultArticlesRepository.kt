package com.example.nytimesapp.data.repositories


import com.example.nytimesapp.data.api.NytAPI
import com.example.nytimesapp.data.vo.ArticleSearchResponse
import com.example.nytimesapp.data.vo.PopularArticlesResponse
import com.example.nytimesapp.other.Constants.NO_INTERNET
import com.example.nytimesapp.other.Constants.SOMETHING_WENT_WRONG
import com.example.nytimesapp.other.Resource
import javax.inject.Inject

class DefaultArticlesRepository @Inject constructor(private val nytAPI: NytAPI) :
    ArticlesRepository {

    /**
     * Popular Articles
     */
    override suspend fun getPopularArticle(type: String): Resource<PopularArticlesResponse> {
        return try {
            val response = nytAPI.getPopularArticles(type)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(SOMETHING_WENT_WRONG, null)
            } else {
                Resource.error(SOMETHING_WENT_WRONG, null)
            }
        } catch (e: Exception) {
            println("Error = "+e.toString())
            Resource.error(NO_INTERNET, null)
        }
    }

    /**
     * Search Articles
     */
    override suspend fun searchForArticle(query: String): Resource<ArticleSearchResponse> {
        return try {
            val response = nytAPI.searchForArticle(query)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(SOMETHING_WENT_WRONG, null)
            } else {
                Resource.error(SOMETHING_WENT_WRONG, null)
            }
        } catch (e: Exception) {
            Resource.error(NO_INTERNET, null)
        }
    }
}














