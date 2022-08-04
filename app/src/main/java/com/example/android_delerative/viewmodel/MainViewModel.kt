package com.example.android_delerative.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_delerative.model.TVShow
import com.example.android_delerative.model.TVShowPopular
import com.example.android_delerative.repository.TVShowRepository
import com.example.android_delerative.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tvShowRepository: TVShowRepository) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val tvShowsFromApi = MutableLiveData<ArrayList<TVShow>>()
    val tvShowsFromDB = MutableLiveData<List<TVShow>>()
    val page = 1
    val tvShowPopular = MutableLiveData<TVShowPopular>()

    init {
        apiTVShowPopular(page)
    }

    /**
     * Retrofit Related
     */

//    fun apiTVShowPopular(page: Int) {
//        isLoading.value = true
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = tvShowRepository.apiTVShowPopular(page)
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//                    val resp = response.body()
//                    tvShowPopular.postValue(resp)
//                    var localShows = tvShowsFromApi.value
//                    if (localShows == null) localShows = ArrayList()
//                    val serverShows = resp!!.tv_shows
//                    localShows.addAll(serverShows)
//
//                    tvShowsFromApi.postValue(localShows)
//                    isLoading.value = false
//                } else {
//                    onError("Error : ${response.message()} ")
//                }
//            }
//        }
//    }

    fun apiTVShowPopular(page: Int) {
        var response : Response<TVShowPopular>
        isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            response = tvShowRepository.apiTVShowPopular(page)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    tvShowPopular.postValue(resp)
                    var localShows = tvShowsFromApi.value
                    if (localShows == null) localShows = ArrayList()
                    val serverShows = resp!!.tv_shows
                    localShows.addAll(serverShows)

                    tvShowsFromApi.postValue(localShows)
                    isLoading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

//    fun getCharacters(): PagingSource<Int, TVShow> {
//        return object : PagingSource<Int, TVShow>() {
//            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TVShow> {
//                val pageNumber = params.key ?: 0
//                val charactersResponse = apiTVShowPopular(page = pageNumber)
//                val characters = charactersResponse
//
//                val prevKey = if (pageNumber > 0) pageNumber - 1 else null
//                val nextKey = if (charactersResponse.info.next != null) pageNumber + 1 else null
//
//                return LoadResult.Page(
//                    data = characters,
//                    prevKey = prevKey,
//                    nextKey = nextKey
//                )
//            }
//
//            override fun getRefreshKey(state: PagingState<Int, TVShow>): Int? {
//                TODO("Not yet implemented")
//            }
//        }
//    }

    private fun onError(message: String) {
        errorMessage.value = message
        isLoading.value = false
    }

    /**
     * Room Related
     */
    fun getTVShowsFromDB() {
        viewModelScope.launch {
            val tvShows = tvShowRepository.getTVShowsFromDB()
            tvShowsFromDB.postValue(tvShows)
        }
    }

    fun insertTVShowToDB(tvShow: TVShow) {
        viewModelScope.launch {
            tvShowRepository.insertTVShowToDB(tvShow)
        }
    }

    fun deleteTvShowsFromDB() {
        viewModelScope.launch {
            tvShowRepository.deleteTvShowsFromDB()
        }
    }
}