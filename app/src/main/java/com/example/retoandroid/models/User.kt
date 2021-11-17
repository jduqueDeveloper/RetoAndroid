package com.example.retoandroid.models

import java.io.Serializable

data class User(
    val username: String,
    val password: String
) : Serializable
