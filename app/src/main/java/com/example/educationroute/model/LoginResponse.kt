package com.example.educationroute.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("role")
    var role: String? = null,

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("client")
    var client: ClientData? = null,
)

data class ClientData(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("email")
    var email: String = "",

    @SerializedName("parentName")
    var parentName: String = "",

    @SerializedName("phone")
    var phone: String = "",

    @SerializedName("childName")
    var childName: String = "",

    @SerializedName("childBirthday")
    var childBirthday: String = ""
)