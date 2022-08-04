package com.example.android_delerative.activity.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.android_delerative.item.itemTVShow
import com.example.android_delerative.model.TVShow
import com.example.android_delerative.viewmodel.MainViewModel


@Composable
fun MainScreen(navController: NavController) {
    val viewModel = hiltViewModel<MainViewModel>()
    val isLoading by viewModel.isLoading.observeAsState(true)
    val tvShows by viewModel.tvShowsFromApi.observeAsState(arrayListOf())

    MainScreenContent(onItemClick = {
        viewModel.insertTVShowToDB(it)
        navController.navigate("details" + "/${it.id}" + "/${it.name}" + "/${it.network}")
    }, isLoading, tvShows)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    onItemClick: ((TVShow) -> Unit)? = null,
    isLoading: Boolean,
    tvShows: List<TVShow>
) {

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                title = { Text("TV Show", color = Color.White) },
            )
        }
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.White
                )
            }
        } else {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                modifier = Modifier.padding(5.dp),
            ) {
                items(tvShows.size) {
                    val tvShow = tvShows[it]
                    itemTVShow(onItemClick, tvShow)
//                    getCharacters(tvShow)
                }
            }
        }
    }
}

//fun getCharacters(tvShow: TVShow): PagingSource<Int, TVShow> {
//    return object : PagingSource<Int, TVShow>() {
//        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TVShow> {
//            val pageNumber = params.key ?: 0
//            val charactersResponse = tvShow
//            val characters = charactersResponse
//
//            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
//            val nextKey = pageNumber + 1
//
//            return LoadResult.Page(
//                data = characters,
//                prevKey = prevKey,
//                nextKey = nextKey
//            )
//        }
//
//        override fun getRefreshKey(state: PagingState<Int, TVShow>): Int? {
//            TODO("Not yet implemented")
//        }
//    }
//}