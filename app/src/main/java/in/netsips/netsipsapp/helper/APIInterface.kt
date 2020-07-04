package `in`.netsips.netsipsapp.helper

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIInterface {
//article metadata
    @Headers("Authorization: Y2FyZS5uZXRzaXBzQGdtYWlsLmNvbTpZVEdwM0VsT2RDOVdpSkpMOEt5Yg==")
    @GET("/")
    fun getMetaData(@Query("url") field: String): Call<MetaResult>

}