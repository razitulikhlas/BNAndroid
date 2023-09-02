package com.razitulikhlas.core.domain.login.repository


import com.razitulikhlas.core.data.source.RemoteDataSource
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.ResponseCheckPhone
import kotlinx.coroutines.flow.Flow


class LoginRepository(
    private val remoteDataSource: RemoteDataSource
) : ILoginRepository {


    override suspend fun checkPhone(phone: String): Flow<ApiResponse<ResponseCheckPhone>>
            =remoteDataSource.checkPhone(phone)




//    override fun getMovies(type: String): Flow<Resource<List<Film>>> =
//        object : NetworkBoundResource<List<Film>, List<ResultsItem>>() {
//            override fun loadFromDB(): Flow<List<Film>> {
//                val data = localDataSource.getAllFilmType(type).map {
//                    MapperMovies.mapEntityToDomain(it)
//                }
//                return data
//            }
//
//            override fun shouldFetch(data: List<Film>?): Boolean =
//                data == null || data.isEmpty()
////                true // ganti dengan true jika ingin selalu mengambil data dari internet
//
//            override suspend fun createCall(): Flow<ApiResponse<List<ResultsItem>>> =
//                remoteDataSource.getMovies()
//
//            override suspend fun saveCallResult(data: List<ResultsItem>) {
//                val filmList = MapperMovies.mapResponseToFilmEntity(data)
//                localDataSource.insertFilm(filmList)
//            }
//        }.asFlow()

    //    override fun getTvShow(type: String): Flow<Resource<List<Film>>> =
//        object : NetworkBoundResource<List<Film>, List<ResultsTvShow>>() {
//            override fun loadFromDB(): Flow<List<Film>> {
//                val data = localDataSource.getAllFilmType(type).map {
//                    MapperMovies.mapEntityToDomain(it)
//                }
//                return data
//            }
//
//            override fun shouldFetch(data: List<Film>?): Boolean =
//                data == null || data.isEmpty()
//            //true// ganti dengan true jika ingin selalu mengambil data dari internet
//
//            override suspend fun createCall(): Flow<ApiResponse<List<ResultsTvShow>>> =
//                remoteDataSource.getTvShow()
//
//            override suspend fun saveCallResult(data: List<ResultsTvShow>) {
//                val filmList = MapperMovies.mapsResponseTvShowToFilmEntity(data)
//                localDataSource.insertFilm(filmList)
//            }
//        }.asFlow()
//
//    override fun getMoviesFavorite(type: String): Flow<List<Film>> {
//        return localDataSource.getMoviesFavorite(type).map {
//            MapperMovies.mapFavoriteEntityToDomain(it)
//        }
//    }
//
//
//    override fun searchMovies(query: String): Flow<Resource<List<Film>>> = flow {
//        when (val apiResponse = remoteDataSource.searchMovies(query).first()) {
//            is ApiResponse.Success -> {
//                val data = MapperMovies.mapResponseSearchToFilm(apiResponse.data)
//                emit(Resource.Success(data))
//            }
//            is ApiResponse.Error -> {
//                emit(Resource.Error(apiResponse.errorMessage))
//            }
//        }
//    }
//
//    override suspend fun saveFavoriteMovies(film: Film) {
//        val data = MapperMovies.mapDomainToFavorite(film)
//        localDataSource.saveFavorite(data)
//    }
//
//    override suspend fun deleteFavoriteMovies(film: Film) {
//        val data = MapperMovies.mapDomainToFavorite(film)
//        localDataSource.deleteFavorite(data)
//    }
//
//    override suspend fun checkFilmExist(id: Int, type: String): Boolean {
//        return localDataSource.checkFilmExist(id,type)
//    }


}