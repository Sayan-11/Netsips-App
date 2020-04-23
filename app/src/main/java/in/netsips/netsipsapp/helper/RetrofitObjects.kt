package `in`.netsips.netsipsapp.helper

import com.google.gson.annotations.SerializedName

class MetaResult {
    @SerializedName("result")
    val result: Result? = null

    @SerializedName("meta")
    val meta: Meta? = null
}

class Result {
    @SerializedName("status")
    val status: String? = null

    @SerializedName("code")
    val code: String? = null

    @SerializedName("reason")
    val reason: String? = null
}

class Site {
    @SerializedName("name")
    val name: String? = null

    @SerializedName("favicon")
    val favicon: String? = null
}

class Meta {
    @SerializedName("site")
    val site: Site? = null

    @SerializedName("description")
    val description: String? = null

    @SerializedName("image")
    val image: String? = null

    @SerializedName("title")
    val title: String? = null
}



