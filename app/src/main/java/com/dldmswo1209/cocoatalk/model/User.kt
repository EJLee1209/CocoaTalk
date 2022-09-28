package com.dldmswo1209.cocoatalk.model

data class User(
    var uid: Int,
    var id: String,
    var password: String,
    var name: String,
    var image: String? = null,
    var state_msg: String? = null
)
