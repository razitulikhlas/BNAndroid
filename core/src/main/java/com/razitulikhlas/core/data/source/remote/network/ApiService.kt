package com.razitulikhlas.core.data.source.remote.network

import com.razitulikhlas.core.data.source.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("api/v1/user/phone/{phone}")
    suspend fun checkPhone(
        @Path("phone") phone: String
    ): ResponseCheckPhone

    @GET("api/v1/disposisi/detail/{id}")
    suspend fun detailDisposisi(
        @Path("id") id: Int
    ): ResponseDetailDisposisi

    @GET("/api/v1/disposisi")
    suspend fun getData(): ResponseData

    @GET("/api/v1/disposisi/{id}")
    suspend fun getDataUser(
        @Path("id") id: String
    ):ResponseData

    @FormUrlEncoded
    @POST("/api/v1/disposisi")
    suspend fun insert(
        @Field("id_user") id_user:Int,
        @Field("pemohon") pemohon:String,
        @Field("ktp_pemohon") ktp_pemohon:String,
        @Field("penjamin") penjamin:String,
        @Field("ktp_penjamin") ktp_penjamin:String,
        @Field("phone") phone:String,
        @Field("skim_kredit") skim_kredit:String,
        @Field("sektor_usaha") sektor_usaha:String,
        @Field("plafond") plafond:String,
        @Field("jangka_waktu") jangka_waktu:Int,
        @Field("status") status:Int = 1,
        @Field("keterangan") keterangan:String? = null,
        @Field("is_delete") is_delete:Int = 0,
    ): ResponseDataSkim

    @FormUrlEncoded
    @POST("/api/v1/disposisi/status/{id}")
    suspend fun updateStatusDisposisi(
        @Path("id") level:Int,
        @Field("status") status:Int,
        @Field("keterangan") keterangan:String,
    ): ResponseNormal

    @GET("/api/v1/disposisi/{skim}/{level}")
    suspend fun getDataSkim(
        @Path("skim") skim:String,
        @Path("level") level:Int
    ):ResponseDataSkim

    @GET("/api/v1/disposisi/search/{pemohon}")
    suspend fun search(
        @Path("pemohon") pemohon:String
    ):ResponseSearch

    @Multipart
    @POST("/api/v1/infousaha")
    suspend fun insertInfoUsaha(
//        @Header("fcm") fcm: String,
//        @Header("Authorization") token: String?,
        @Part("id_disposisi") name_product: RequestBody?,
        @Part("latitude") category: RequestBody?,
        @Part("longititude") price: RequestBody?,
        @Part("address") price_promo: RequestBody?,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image4: MultipartBody.Part?,
        @Part image5: MultipartBody.Part?,
        @Part image6: MultipartBody.Part?,
        @Part image7: MultipartBody.Part?,
        @Part image8: MultipartBody.Part?,
    ): ResponseInsertInfoUsaha
}